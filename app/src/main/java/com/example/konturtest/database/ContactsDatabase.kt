package com.example.konturtest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.konturtest.database.entity.Contact

/**
 * Created by Vladimir Kraev
 */

@Database(entities = (arrayOf(Contact::class)), version = 1)
abstract class ContactsDatabase : RoomDatabase() {

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

