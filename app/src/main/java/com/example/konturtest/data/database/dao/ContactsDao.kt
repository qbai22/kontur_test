package com.example.konturtest.data.database.dao

import androidx.room.*
import com.example.konturtest.data.database.entity.Contact
import io.reactivex.Single

/**
 * Created by Vladimir Kraev
 */
@Dao
interface ContactsDao {

    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Single<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    fun getContactById(id: String): Contact

    @Query("SELECT * FROM contacts WHERE name LIKE :text OR phone LIKE :text")
    fun getContactsByNameOrPhone(text: String): Single<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(contacts: List<Contact>)

    @Delete
    fun delete(contact: Contact)

}

