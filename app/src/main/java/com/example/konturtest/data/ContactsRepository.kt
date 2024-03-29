package com.example.konturtest.data

import com.example.konturtest.data.local.room.entity.Contact
import io.reactivex.Single

/**
 * Created by Vladimir Kraev
 */
interface ContactsRepository {

    fun getContact(contactId: String): Single<Contact>

    fun getContacts(isForceLoad: Boolean): Single<List<Contact>>

    fun getFilteredContacts(input: String): Single<List<Contact>>

}