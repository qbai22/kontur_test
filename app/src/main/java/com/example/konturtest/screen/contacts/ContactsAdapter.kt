package com.example.konturtest.screen.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.konturtest.R
import com.example.konturtest.model.Contact
import kotlinx.android.synthetic.main.item_contact.view.*

/**
 * Created by Vladimir Kraev
 */
class ContactsAdapter(
    private val listener: OnContactClickListener
) : RecyclerView.Adapter<ContactsAdapter.ContactHolder>() {

    private val contacts: MutableList<Contact> = ArrayList()

    interface OnContactClickListener {
        fun onContactClick(contact: Contact)
    }

    fun changeDataset(updatedContacts: List<Contact>) {
        contacts.clear()
        contacts.addAll(updatedContacts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactHolder(itemView)
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bindContact(contacts[position])
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
}