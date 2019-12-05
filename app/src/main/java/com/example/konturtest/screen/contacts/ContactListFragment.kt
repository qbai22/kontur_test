package com.example.konturtest.screen.contacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.konturtest.R
import com.example.konturtest.data.local.room.entity.Contact
import com.example.konturtest.databinding.FragmentContactListBinding
import com.example.konturtest.screen.common.ErrorView
import com.example.konturtest.utils.ErrorEvent
import com.example.konturtest.utils.NavigateToContactDetailsEvent
import com.google.android.material.snackbar.Snackbar


/**
 * Created by Vladimir Kraev
 */

class ContactListFragment : Fragment(), ErrorView, SearchView.OnQueryTextListener {

    private lateinit var binding: FragmentContactListBinding

    private lateinit var viewModel: ContactListViewModel
    private lateinit var contactListAdapter: ContactListAdapter

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(true)
        }

        setHasOptionsMenu(true)
        viewModel = ViewModelProviders.of(this).get(ContactListViewModel::class.java)
        contactListAdapter = ContactListAdapter(viewModel)

        binding = FragmentContactListBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
        }

        with(binding) {
            contactListRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = contactListAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        setupAdapter()

        viewModel.contactsData.observe(viewLifecycleOwner,
            Observer<List<Contact>> { contacts -> contactListAdapter.submitList(contacts) })

        viewModel.errorEvent.observe(viewLifecycleOwner,
            Observer<ErrorEvent> {
                it.getContentIfNotHandled()?.let { errorMsg -> showError(errorMsg) }
            })

        viewModel.navigateToContactDetailsEvent.observe(viewLifecycleOwner,
            Observer<NavigateToContactDetailsEvent> {
                it.getContentIfNotHandled()?.let { contactId -> openContactDetails(contactId) }
            })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_contact_list, menu)
        val searchItem = menu.findItem(R.id.search_item)
        val searchView: SearchView = searchItem.actionView as SearchView
        setupSearchView(searchView, searchItem)
    }

    private fun setupAdapter() {
        contactListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.contactListRecyclerView.scrollToPosition(0)
            }
        })
    }

    private fun setupSearchView(searchView: SearchView, searchMenuItem: MenuItem) {

        searchView.apply {
            setOnQueryTextListener(this@ContactListFragment)
            queryHint = getString(R.string.search_hint)
            maxWidth = Integer.MAX_VALUE
        }
        val previousFilter = viewModel.getFilterInput()
        previousFilter?.let {
            if (it.isNotEmpty()) {
                searchMenuItem.expandActionView()
                searchView.setQuery(previousFilter, false)
            }
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        viewModel.filterContacts(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        viewModel.filterContacts(newText)
        return true
    }

    private fun openContactDetails(contactId: String) {
        val action =
            ContactListFragmentDirections.actionContactListFragmentToContactDetailsFragment(
                contactId
            )
        findNavController().navigate(action)
    }


    override fun showError(message: Int) {
        val snackbar = Snackbar.make(view!!, message, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.try_again) {
            viewModel.reload()
            snackbar.dismiss()
        }
        snackbar.show()
    }

}