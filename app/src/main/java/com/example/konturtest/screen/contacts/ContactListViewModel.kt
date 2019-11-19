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

    private val showErrorSnackbar = MutableLiveData<ErrorEvent>()

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
            .subscribe(
                { contacts ->
                    contactsData.value = contacts
                    isLoading.set(false)
                    Log.e("Contacts View model", "downloaded ${contacts.size}")
                }, { e ->
                    isLoading.set(false)
                    showErrorSnackbar.value = ErrorEvent(R.string.error_download)
                    e.printStackTrace()
                })
    }

    fun observeErrors() = showErrorSnackbar

    fun onReloadClick() {
        loadContacts(true)
    }

    companion object {
        private const val TAG = "CONTACTS_VIEW_MODEL"
    }

}