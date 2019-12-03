package com.example.konturtest.data.http

import com.example.konturtest.data.local.room.entity.Contact
import io.reactivex.Single

/**
 * Created by Vladimir Kraev
 */
interface RemoteContactsDataSource {

    fun loadContacts(): Single<List<Contact>>

}