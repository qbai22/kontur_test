package com.example.konturtest.data.repository

import android.annotation.SuppressLint
import com.example.konturtest.data.TimeProvider
import com.example.konturtest.data.database.ContactsDatabase
import com.example.konturtest.data.database.entity.Contact
import com.example.konturtest.data.http.ApiCreator
import com.example.konturtest.data.http.dto.DtoContact
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers

/**
 * Created by Vladimir Kraev
 */
class DefaultContactsRepository(
    apiCreator: ApiCreator,
    contactsDatabase: ContactsDatabase,
    private val loadTimeProvider: TimeProvider
) : ContactsRepository {

    private val api = apiCreator.getApi()
    private val dao = contactsDatabase.contactsDao()

    @SuppressLint("CheckResult")
    override fun getContacts(isForceLoad: Boolean): Single<List<Contact>> {

        //Checks did application already load some data before or not
        val isFirstLoad = loadTimeProvider.getLastLoadTime() == TimeProvider.EMPTY_LOAD_TIME
        val isTimeForReload = checkIfTimeForReload()

        //Load time update and cashing only if loading was successful
        if (isFirstLoad || isForceLoad || isTimeForReload) return loadRemoteContacts().doOnSuccess {
            loadTimeProvider.saveLastLoadTime(System.currentTimeMillis())
            saveContacts(it)
        }

        return loadLocalContacts()
    }

    private fun loadRemoteContacts(): Single<List<Contact>> {

        val firstRequest = api.getContactsFirstSource().map { mapDtoContacts(it) }
        val secondRequest = api.getContactsSecondSource().map { mapDtoContacts(it) }
        val thirdRequest = api.getContactsThirdSource().map { mapDtoContacts(it) }

        return Single.zip(firstRequest, secondRequest, thirdRequest,
            Function3 { firstList: List<Contact>,
                        secondList: List<Contact>,
                        thirdList: List<Contact> ->

                val contacts: List<Contact> = ArrayList<Contact>().also {
                    it.addAll(firstList)
                    it.addAll(secondList)
                    it.addAll(thirdList)
                }
                contacts
            }
        )
            .subscribeOn(Schedulers.io())
    }

    private fun loadLocalContacts(): Single<List<Contact>> =
        dao.getAllContacts().subscribeOn(Schedulers.io())

    private fun saveContacts(contacts: List<Contact>) = dao.insertAll(contacts)

    private fun checkIfTimeForReload(): Boolean {

        val lastLoadTime = loadTimeProvider.getLastLoadTime()
        val currentTime = System.currentTimeMillis()

        return lastLoadTime != 0L &&
                currentTime - lastLoadTime > RELOAD_TIME_IN_MILLIS
    }

    private fun mapDtoContacts(dtoContacts: List<DtoContact>): List<Contact> {

        val contacts = ArrayList<Contact>()

        dtoContacts.forEach {
            val contact = Contact(
                it.id,
                it.name,
                it.phone,
                it.height,
                it.bio,
                it.temper,
                it.edPeriod.start,
                it.edPeriod.end
            )
            contacts.add(contact)
        }

        return contacts
    }

    companion object {
        private const val TAG = "CONTACTS_REPO"
        private const val RELOAD_TIME_IN_MILLIS = 60000
    }

}
