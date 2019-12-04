package com.example.konturtest.data.di

import android.app.Application
import android.content.Context
import com.example.konturtest.data.time.TimePreferences
import com.example.konturtest.data.time.TimeProvider
import com.example.konturtest.data.http.GithubContactsSource
import com.example.konturtest.data.http.RemoteContactsDataSource
import com.example.konturtest.data.http.api.GithubApiCreator
import com.example.konturtest.data.http.mapper.ContactMapper
import com.example.konturtest.data.http.mapper.DefaultContactMapper
import com.example.konturtest.data.local.LocalContactsDataSource
import com.example.konturtest.data.local.RoomContactsSource
import com.example.konturtest.data.local.room.ContactsDatabase
import com.example.konturtest.data.ContactsRepository
import com.example.konturtest.data.DefaultContactsRepository
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
    fun provideApiCreator(): GithubApiCreator =
        GithubApiCreator()

    @Provides
    @Singleton
    fun provideTimeProvider(context: Context): TimeProvider =
        TimePreferences(context)

    @Provides
    @Singleton
    fun provideContactMapper(): ContactMapper = DefaultContactMapper()

    @Provides
    @Singleton
    fun provideRemoteContactsDataSource(apiCreator: GithubApiCreator, contactMapper: ContactMapper): RemoteContactsDataSource =
        GithubContactsSource(apiCreator, contactMapper)

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
        DefaultContactsRepository(
            localContactsDataSource,
            remoteContactsDataSource,
            timeProvider
        )

}