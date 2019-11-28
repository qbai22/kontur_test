package com.example.konturtest.screen.contacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.konturtest.R
import com.example.konturtest.data.database.entity.Contact
import com.example.konturtest.databinding.FragmentContactListBinding
import com.example.konturtest.screen.common.ErrorView
import com.example.konturtest.utils.ErrorEvent
import com.example.konturtest.utils.NavigateToContactDetailsEvent
import com.google.android.material.snackbar.Snackbar


/**
 * Created by Vladimir Kraev
 */

class ContactListFragment : Fragment(), ErrorView {

    private lateinit var binding: FragmentContactListBinding

    private lateinit var viewModel: ContactListViewModel
    private lateinit var contactListAdapter: ContactListAdapter

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
                it.getContentIfNotHandled()?.let { errorMsg ->
                    showError(errorMsg)
                }
            }
        )

        viewModel.navigateToContactDetailsEvent.observe(viewLifecycleOwner,
            Observer<NavigateToContactDetailsEvent> {
                it.getContentIfNotHandled()?.let { contactId ->
                    openContactDetails(contactId)
                }
            }
        )

    }

    private fun setupAdapter() {
        contactListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.contactListRecyclerView.scrollToPosition(0)
            }
        })
    }


    private fun openContactDetails(contactId: String) {
        val action =
            ContactListFragmentDirections.actionContactListFragmentToContactDetailsFragment(
                contactId
            )
        findNavController().navigate(action)
    }


    override fun showError(message: Int) {
        val snackbar = Snackbar.make(view!!, message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.try_again) {
            viewModel.reload()
            snackbar.dismiss()
        }
        snackbar.show()
    }

}