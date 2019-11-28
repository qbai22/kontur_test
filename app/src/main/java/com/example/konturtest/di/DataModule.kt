package com.example.konturtest.di

import android.app.Application
import android.content.Context
import com.example.konturtest.data.TimePreferences
import com.example.konturtest.data.TimeProvider
import com.example.konturtest.data.database.ContactsDatabase
import com.example.konturtest.data.http.ApiCreator
import com.example.konturtest.data.mapper.ContactMapper
import com.example.konturtest.data.mapper.DefaultContactMapper
import com.example.konturtest.data.repository.ContactsRepository
import com.example.konturtest.data.repository.DefaultContactsRepository
import dagger.Module
import dagger.Provides
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
    fun provideTimeProvider(context: Context): TimeProvider =
        TimePreferences(context)

    @Provides
    @Singleton
    fun provideContactMapper(): ContactMapper = DefaultContactMapper()

    @Provides
    @Singleton
    fun provideContactsRepository(
        apiCreator: ApiCreator,
        contactsDatabase: ContactsDatabase,
        timeProvider: TimeProvider,
        contactMapper: ContactMapper
    ): ContactsRepository =
        DefaultContactsRepository(apiCreator, contactsDatabase, timeProvider, contactMapper)

}