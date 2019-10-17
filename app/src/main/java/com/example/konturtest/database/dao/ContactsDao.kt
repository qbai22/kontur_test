package com.example.konturtest.database.dao

import androidx.room.*
import com.example.konturtest.database.entity.Contact
import io.reactivex.Single

/**
 * Created by Vladimir Kraev
 */
@Dao
interface ContactsDao {

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Single<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    fun getContactById(id: String): Single<Contact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    @Delete
    fun delete(contact: Contact)

}

