package com.example.konturtest.data.repository

import com.example.konturtest.data.database.entity.Contact
import io.reactivex.Single

/**
 * Created by Vladimir Kraev
 */
interface ContactsRepository {

    fun getContacts(isForceLoad: Boolean): Single<List<Contact>>

}