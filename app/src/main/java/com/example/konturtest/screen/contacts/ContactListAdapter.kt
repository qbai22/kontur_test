package com.example.konturtest.screen.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.konturtest.R
import com.example.konturtest.data.database.entity.Contact
import kotlinx.android.synthetic.main.item_contact.view.*

/**
 * Created by Vladimir Kraev
 */
class ContactListAdapter(
    private val listener: OnContactClickListener
) : ListAdapter<Contact, ContactListAdapter.ContactHolder>(ContactDiffCallback()) {

    interface OnContactClickListener {
        fun onContactClick(contact: Contact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bindContact(getItem(position))
    }

    companion object {
        private const val TAG = "CONTACTS_ADAPTER"
    }

    inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var contact: Contact

        init {
            itemView.setOnClickListener { listener.onContactClick(contact) }
        }

        fun bindContact(contact: Contact) {
            this.contact = contact
            with(itemView) {
                name_text_view.text = contact.name
                phone_text_view.text = contact.phone
                height_text_view.text = contact.height.toString()
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