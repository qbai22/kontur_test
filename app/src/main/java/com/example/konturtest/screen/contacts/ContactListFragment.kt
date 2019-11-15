package com.example.konturtest.screen.contacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.konturtest.R
import com.example.konturtest.data.database.entity.Contact
import com.example.konturtest.databinding.FragmentContactListBinding
import com.example.konturtest.screen.common.ErrorView
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Vladimir Kraev
 */

class ContactListFragment :
    Fragment(),
    ContactListAdapter.OnContactClickListener,
    ErrorView {

    private lateinit var binding: FragmentContactListBinding

    private lateinit var searchView: SearchView
    private lateinit var contactListRecyclerView: RecyclerView
    private lateinit var viewModel: ContactListViewModel
    private val contactListAdapter = ContactListAdapter(this)

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this).get(ContactListViewModel::class.java)

        binding = FragmentContactListBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
        }

        with(binding) {
            contactListRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = contactListAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }

        viewModel.startObserving()
        viewModel.contactsData.observe(this,
            Observer<List<Contact>> { contacts -> contactListAdapter.changeDataset(contacts) })
        viewModel.observeErrors().observe(this, Observer {
            it.getContentIfNotHandled()?.let { errorMsg ->
                showError(errorMsg)
            }
        })

        return binding.root
    }

    override fun onContactClick(contact: Contact) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: Int) {
        val snackbar = Snackbar.make(view!!, message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.try_again) {
            viewModel.onReloadClick()
            snackbar.dismiss()
        }
        snackbar.show()
    }

    companion object {
        private const val TAG = "CONGRATS_FRAG"

        fun newInstance() = ContactListFragment()
    }

}