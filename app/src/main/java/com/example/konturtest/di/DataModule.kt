package com.example.konturtest.di

import android.app.Application
import android.content.Context
import com.example.konturtest.database.ContactsDatabase
import com.example.konturtest.http.ApiCreator
import dagger.Module
import dagger.Provides
import xunison.com.smarthomeapp.repository.ContactsRepository
import javax.inject.Singleton

/**
 * Created by Vladimir Kraev
 */
@Module
class DataModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = application

    @Provides
    @Singleton
    fun provideContactsDatabase(context: Context): ContactsDatabase =
        ContactsDatabase.create(context)

    @Provides
    @Singleton
    fun provideApiCreator(): ApiCreator = ApiCreator()

    @Provides
    @Singleton
    fun provideContactsRepository(
        apiCreator: ApiCreator,
        contactsDatabase: ContactsDatabase
    ): ContactsRepository = ContactsRepository(apiCreator, contactsDatabase)

}