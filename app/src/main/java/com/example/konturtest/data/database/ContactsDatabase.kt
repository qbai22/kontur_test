package com.example.konturtest.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.konturtest.data.database.dao.ContactsDao
import com.example.konturtest.data.database.entity.Contact

/**
 * Created by Vladimir Kraev
 */

@Database(entities = (arrayOf(Contact::class)), version = 2)
abstract class ContactsDatabase : RoomDatabase() {

    abstract fun contactsDao(): ContactsDao

    companion object {
        private const val DB_NAME = "contacts_database"

        fun create(context: Context): ContactsDatabase {
            val builder = Room.databaseBuilder(
                context.applicationContext,
                ContactsDatabase::class.java,
                DB_NAME
            )
                .fallbackToDestructiveMigration()
            return builder.build()
        }
    }

}

