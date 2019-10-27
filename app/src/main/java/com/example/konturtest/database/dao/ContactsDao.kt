package com.example.konturtest.database.dao

import androidx.room.*
import com.example.konturtest.database.entity.Contact

/**
 * Created by Vladimir Kraev
 */
@Dao
interface ContactsDao {

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): List<Contact>

    @Query("SELECT * FROM contacts WHERE id = :id")
    fun getContactById(id: String): Contact

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(contacts : List<Contact>)

    @Delete
    fun delete(contact: Contact)

}

