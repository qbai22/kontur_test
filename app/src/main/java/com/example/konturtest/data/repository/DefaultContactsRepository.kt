package com.example.konturtest.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.konturtest.data.TimeProvider
import com.example.konturtest.data.database.ContactsDatabase
import com.example.konturtest.data.database.entity.Contact
import com.example.konturtest.data.http.ApiCreator
import com.example.konturtest.data.http.dto.DtoContact
import com.example.konturtest.data.mapper.ContactMapper
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Vladimir Kraev
 */
class DefaultContactsRepository(
    apiCreator: ApiCreator,
    contactsDatabase: ContactsDatabase,
    private val loadTimeProvider: TimeProvider,
    private val contactMapper: ContactMapper
) : ContactsRepository {

    private val api = apiCreator.getApi()
    private val dao = contactsDatabase.contactsDao()


    @SuppressLint("CheckResult")
    override fun getContact(contactId: String): Single<Contact> =
        dao.getContactById(contactId)
            .subscribeOn(Schedulers.io())

    @SuppressLint("CheckResult")
    override fun getContacts(isForceLoad: Boolean): Single<List<Contact>> {

        //Checks did application already load some data before or not
        val isFirstLoad = loadTimeProvider.getLastLoadTime() == TimeProvider.EMPTY_LOAD_TIME
        val isTimeForReload = checkIfTimeForReload()

        //Last load time updating and cashing contacts only if loading was successful
        if (isFirstLoad || isForceLoad || isTimeForReload) {
            Log.w(
                TAG,
                "isFirstLoad = $isFirstLoad isForceLoad = $isForceLoad isTimeForReload = $isTimeForReload"
            )
            return loadRemoteContacts()
                .doOnSuccess {
                    loadTimeProvider.saveLastLoadTime(System.currentTimeMillis())
                    saveContacts(it)
                }
        }

        return loadLocalContacts()
    }

    override fun getFilteredContacts(input: CharSequence): Single<List<Contact>> =
        dao.getContactsByNameOrPhone("%$input%")
            .subscribeOn(Schedulers.io())

    private fun loadRemoteContacts(): Single<List<Contact>> {

        val firstRequest = api.getContactsFirstSource().map { contactMapper.mapContacts(it) }
        val secondRequest = api.getContactsSecondSource().map { contactMapper.mapContacts(it) }
        val thirdRequest = api.getContactsThirdSource().map { contactMapper.mapContacts(it) }

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
        Log.w(TAG, "last loadTime = ${Date(lastLoadTime)} currentTime = ${Date(currentTime)}")
        return lastLoadTime != TimeProvider.EMPTY_LOAD_TIME &&
                currentTime - lastLoadTime > RELOAD_TIME_IN_MILLIS
    }

    companion object {
        private const val TAG = "CONTACTS_REPO"
        private const val RELOAD_TIME_IN_MILLIS = 60000
    }

}
