package com.example.konturtest.screen.contacts

import android.annotation.SuppressLint
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.konturtest.ContactsApp
import com.example.konturtest.R
import com.example.konturtest.data.database.entity.Contact
import com.example.konturtest.data.repository.ContactsRepository
import com.example.konturtest.utils.ErrorEvent
import com.example.konturtest.utils.NavigateToContactDetailsEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by Vladimir Kraev
 */

class ContactListViewModel : ViewModel() {

    @Inject
    lateinit var contactsRepository: ContactsRepository

    val isLoading = ObservableField(false)
    val isRefreshing = ObservableField(false)

    private val _contactsData = MutableLiveData<List<Contact>>()
    val contactsData: LiveData<List<Contact>> = _contactsData

    private val _errorEvent = MutableLiveData<ErrorEvent>()
    val errorEvent: LiveData<ErrorEvent> = _errorEvent

    private val _navigateToContactDetailsEvent = MutableLiveData<NavigateToContactDetailsEvent>()
    val navigateToContactDetailsEvent: LiveData<NavigateToContactDetailsEvent> =
        _navigateToContactDetailsEvent

    init {
        ContactsApp.instance.getDataComponent().inject(this@ContactListViewModel)
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
            .doOnError { _errorEvent.value = ErrorEvent(R.string.error_download) }
            .subscribe(
                { contacts -> _contactsData.value = contacts },
                { e -> e.printStackTrace() })
    }

    @SuppressLint("CheckResult")
    fun refreshContacts() {

        isRefreshing.set(true)
        contactsRepository
            .getContacts(isForceLoad = true)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { isRefreshing.set(false) }
            .doOnError { _errorEvent.value = ErrorEvent(R.string.error_download) }
            .subscribe(
                { contacts -> _contactsData.value = contacts },
                { e -> e.printStackTrace() })
    }

    @SuppressLint("CheckResult")
    fun filterContacts(input: CharSequence) {

        contactsRepository.getFilteredContacts(input)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { contacts -> _contactsData.value = contacts },
                { e -> e.printStackTrace() })
    }

    fun reload() {
        loadContacts(isForceReload = true)
    }

    fun openContactDetails(contactId: String) {
        _navigateToContactDetailsEvent.value = NavigateToContactDetailsEvent(contactId)
    }

    companion object {
        private const val TAG = "CONTACTS_VIEW_MODEL"
    }

}