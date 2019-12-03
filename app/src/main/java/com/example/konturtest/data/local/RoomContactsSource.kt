package com.example.konturtest.data.local

import com.example.konturtest.data.local.room.ContactsDatabase
import com.example.konturtest.data.local.room.entity.Contact
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Created by Vladimir Kraev
 */

internal class RoomContactsSource(
    contactsDatabase: ContactsDatabase
) : LocalContactsDataSource {

    private val dao = contactsDatabase.contactsDao()

    override fun getContacts(): Single<List<Contact>> =
        dao.getAllContacts().subscribeOn(Schedulers.io())

    override fun getContactById(contactId: String): Single<Contact> =
        dao.getContactById(contactId).subscribeOn(Schedulers.io())

    override fun getContactsByNameOrPhone(text: CharSequence): Single<List<Contact>> =
        dao.getContactsByNameOrPhone("%$text%").subscribeOn(Schedulers.io())

    override fun saveContacts(contacts: List<Contact>) =
        dao.insertAll(contacts)

}