package com.example.konturtest

import android.app.Application
import com.example.konturtest.di.DaggerDataComponent
import com.example.konturtest.di.DataComponent
import com.example.konturtest.di.DataModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Created by Vladimir Kraev
 */
class ContactsApp : Application() {


    private lateinit var dataComponent: DataComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        dataComponent = DaggerDataComponent.builder()
            .dataModule(DataModule(this))
            .build()
    }

    fun getDataComponent() = dataComponent

    companion object {
        private const val TAG = "CONTACTS_APP"
        lateinit var instance: ContactsApp
            private set
    }

}

