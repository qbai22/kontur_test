package com.example.konturtest.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.konturtest.data.local.room.entity.Contact

/**
 * Created by Vladimir Kraev
 */

@Database(entities = (arrayOf(Contact::class)), version = 3, exportSchema = false)
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

