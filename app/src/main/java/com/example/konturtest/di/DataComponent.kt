package com.example.konturtest.di

import com.example.konturtest.screen.contacts.ContactListViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Vladimir Kraev
 */
@Singleton
@Component(modules = [DataModule::class])
interface DataComponent {

    fun inject(contactListViewModel: ContactListViewModel)

}