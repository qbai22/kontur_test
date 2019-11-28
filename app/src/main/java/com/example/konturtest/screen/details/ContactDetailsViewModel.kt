package com.example.konturtest.screen.details

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.konturtest.ContactsApp
import com.example.konturtest.data.database.entity.Contact
import com.example.konturtest.data.repository.ContactsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by Vladimir Kraev
 */
class ContactDetailsViewModel : ViewModel() {

    @Inject
    lateinit var contactsRepository: ContactsRepository

    val name = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val temperament = MutableLiveData<String>()
    val startEducation = MutableLiveData<String>()
    val endEducation = MutableLiveData<String>()
    val biography = MutableLiveData<String>()

    init {
        ContactsApp.instance.getDataComponent().inject(this@ContactDetailsViewModel)
    }

    @SuppressLint("CheckResult")
    fun start(contactId: String) {

        contactsRepository.getContact(contactId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { setupContact(it) }
            .subscribe()
    }

    private fun setupContact(contact: Contact) {
        name.value = contact.name
        phone.value = contact.phone
        temperament.value = contact.temperament
        startEducation.value = contact.startEducation
        endEducation.value = contact.endEducation
        biography.value = contact.bio
    }

    companion object {
        private const val TAG = "CONTACT_DETAILS_VIEW_MO"
    }

}