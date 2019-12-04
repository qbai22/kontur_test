package com.example.konturtest.screen.contacts

import android.annotation.SuppressLint
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.konturtest.ContactsApp
import com.example.konturtest.R
import com.example.konturtest.data.ContactsRepository
import com.example.konturtest.data.local.room.entity.Contact
import com.example.konturtest.utils.ErrorEvent
import com.example.konturtest.utils.NavigateToContactDetailsEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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

    private val dataCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private var filterInput: String? = null

    init {
        ContactsApp.instance.getDataComponent().inject(this@ContactListViewModel)
        // Set initial state
        loadContacts(isForceReload = false)
    }

    @SuppressLint("CheckResult")
    private fun loadContacts(isForceReload: Boolean) {

        dataCompositeDisposable.clear()
        isLoading.set(true)
        dataCompositeDisposable.add(
            contactsRepository
                .getContacts(isForceReload)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { isLoading.set(false) }
                .doOnError { _errorEvent.value = ErrorEvent(R.string.error_download) }
                .subscribe(
                    { contacts -> _contactsData.value = contacts },
                    { e -> e.printStackTrace() })
        )
    }

    @SuppressLint("CheckResult")
    fun refreshContacts() {

        dataCompositeDisposable.clear()
        isRefreshing.set(true)
        dataCompositeDisposable.add(
            contactsRepository
                .getContacts(isForceLoad = true)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { isRefreshing.set(false) }
                .doOnError { _errorEvent.value = ErrorEvent(R.string.error_download) }
                .subscribe(
                    { contacts -> _contactsData.value = contacts },
                    { e -> e.printStackTrace() })
        )
    }

    @SuppressLint("CheckResult")
    fun filterContacts(input: String) {
        filterInput = input
        dataCompositeDisposable.clear()
        dataCompositeDisposable.add(
            contactsRepository.getFilteredContacts(input)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { contacts -> _contactsData.value = contacts },
                    { e -> e.printStackTrace() })
        )
    }

    fun getFilterInput() = filterInput

    fun reload() {
        loadContacts(isForceReload = true)
    }

    fun openContactDetails(contactId: String) {
        _navigateToContactDetailsEvent.value = NavigateToContactDetailsEvent(contactId)
    }

    override fun onCleared() {
        dataCompositeDisposable.clear()
    }

    companion object {
        private const val TAG = "CONTACTS_VIEW_MODEL"
    }

}