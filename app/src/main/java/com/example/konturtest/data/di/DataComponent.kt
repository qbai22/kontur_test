package com.example.konturtest.data.di

import com.example.konturtest.screen.contacts.ContactListViewModel
import com.example.konturtest.screen.details.ContactDetailsViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Vladimir Kraev
 */
@Singleton
@Component(modules = [DataModule::class])
interface DataComponent {

    fun inject(contactListViewModel: ContactListViewModel)
    fun inject(contactDetailsViewModel: ContactDetailsViewModel)

}