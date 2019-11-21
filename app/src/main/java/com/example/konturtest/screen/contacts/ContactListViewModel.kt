package com.example.konturtest.screen.contacts

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.konturtest.ContactsApp
import com.example.konturtest.R
import com.example.konturtest.data.database.entity.Contact
import com.example.konturtest.data.repository.ContactsRepository
import com.example.konturtest.utils.ErrorEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by Vladimir Kraev
 */

class ContactListViewModel : ViewModel() {

    @Inject
    lateinit var contactsRepository: ContactsRepository

    val contactsData: MutableLiveData<List<Contact>> = MutableLiveData()
    val isLoading = ObservableField(false)
    val isRefreshing = ObservableField(false)
    private val errorData = MutableLiveData<ErrorEvent>()

    init {
        ContactsApp.instance.getDataComponent().inject(this)
        // Set initial state
        loadContacts(false)
    }

    @SuppressLint("CheckResult")
    fun loadContacts(isForceReload: Boolean) {

        isLoading.set(true)
        contactsRepository
            .getContacts(isForceReload)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { isLoading.set(false) }
            .doOnError { errorData.value = ErrorEvent(R.string.error_download) }
            .subscribe(
                { contacts -> contactsData.value = contacts },
                { e -> e.printStackTrace() })
    }

    @SuppressLint("CheckResult")
    fun refreshContacts() {

        isRefreshing.set(true)
        contactsRepository
            .getContacts(isForceLoad = true)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { isRefreshing.set(false) }
            .doOnError { errorData.value = ErrorEvent(R.string.error_download) }
            .subscribe(
                { contacts -> contactsData.value = contacts },
                { e -> e.printStackTrace() })
    }

    @SuppressLint("CheckResult")
    fun filterContacts(input: String) {

        contactsRepository.getFilteredContacts(input)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { contacts -> contactsData.value = contacts },
                { e -> e.printStackTrace() })
    }

    fun observeErrors() = errorData

    fun reload() {
        loadContacts(isForceReload = true)
    }

    companion object {
        private const val TAG = "CONTACTS_VIEW_MODEL"
    }

}