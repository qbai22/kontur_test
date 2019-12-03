package com.example.konturtest.data.di

import android.app.Application
import android.content.Context
import com.example.konturtest.data.TimePreferences
import com.example.konturtest.data.TimeProvider
import com.example.konturtest.data.http.GithubContactsDataSource
import com.example.konturtest.data.http.RemoteContactsDataSource
import com.example.konturtest.data.http.api.ApiCreator
import com.example.konturtest.data.http.mapper.ContactMapper
import com.example.konturtest.data.http.mapper.DefaultContactMapper
import com.example.konturtest.data.local.LocalContactsDataSource
import com.example.konturtest.data.local.RoomContactsSource
import com.example.konturtest.data.local.room.ContactsDatabase
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
    fun provideApiCreator(): ApiCreator =
        ApiCreator()

    @Provides
    @Singleton
    fun provideTimeProvider(context: Context): TimeProvider =
        TimePreferences(context)

    @Provides
    @Singleton
    fun provideContactMapper(): ContactMapper = DefaultContactMapper()

    @Provides
    @Singleton
    fun provideRemoteContactsDataSource(apiCreator: ApiCreator, contactMapper: ContactMapper): RemoteContactsDataSource =
        GithubContactsDataSource(apiCreator, contactMapper)

    @Provides
    @Singleton
    fun provideLocalContactsDataSource(contactsDatabase: ContactsDatabase): LocalContactsDataSource =
        RoomContactsSource(contactsDatabase)

    @Provides
    @Singleton
    fun provideContactsRepository(
        localContactsDataSource: LocalContactsDataSource,
        remoteContactsDataSource: RemoteContactsDataSource,
        timeProvider: TimeProvider
    ): ContactsRepository =
        DefaultContactsRepository (localContactsDataSource, remoteContactsDataSource, timeProvider)

}