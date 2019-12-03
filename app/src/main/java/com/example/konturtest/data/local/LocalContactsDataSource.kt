package com.example.konturtest.data.local

import com.example.konturtest.data.local.room.entity.Contact
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Vladimir Kraev
 */

interface LocalContactsDataSource {

    fun getContactById(contactId: String): Single<Contact>

    fun getFilteredContacts(text: CharSequence): Single<List<Contact>>

    fun getContacts(): Single<List<Contact>>

    fun saveContacts(contacts: List<Contact>)

}