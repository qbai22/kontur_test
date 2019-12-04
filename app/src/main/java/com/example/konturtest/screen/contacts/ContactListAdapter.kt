package com.example.konturtest.screen.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.konturtest.data.local.room.entity.Contact
import com.example.konturtest.databinding.ItemContactBinding

/**
 * Created by Vladimir Kraev
 */
class ContactListAdapter(private val viewModel: ContactListViewModel) :
    ListAdapter<Contact, ContactListAdapter.ContactHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContactHolder.from(parent)

    override fun onBindViewHolder(holder: ContactHolder, position: Int) =
        holder.bindContact(viewModel, getItem(position))

    companion object {
        private const val TAG = "CONTACTS_ADAPTER"
    }

    class ContactHolder private constructor(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindContact(viewModel: ContactListViewModel, contact: Contact) {
            binding.viewModel = viewModel
            binding.contact = contact
        }

        companion object {
            fun from(parent: ViewGroup): ContactHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemContactBinding.inflate(inflater, parent, false)
                return ContactHolder(binding)
            }
        }
    }

    /**
     * As well as [Contact] is a data class we can use '==' to compare content without overriding equals()
     */
    class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

    }

}