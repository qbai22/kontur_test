package com.example.konturtest.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.konturtest.data.TimeProvider
import com.example.konturtest.data.http.RemoteContactsDataSource
import com.example.konturtest.data.local.LocalContactsDataSource
import com.example.konturtest.data.local.room.entity.Contact
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by Vladimir Kraev
 */
class DefaultContactsRepository(
    private val localContactsDataSource: LocalContactsDataSource,
    private val remoteContactsDataSource: RemoteContactsDataSource,
    private val loadTimeProvider: TimeProvider
) : ContactsRepository {

    @SuppressLint("CheckResult")
    override fun getContact(contactId: String): Single<Contact> =
        localContactsDataSource.getContactById(contactId)

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
            return remoteContactsDataSource.loadContacts()
                .doOnSuccess {
                    loadTimeProvider.saveLastLoadTime(System.currentTimeMillis())
                    localContactsDataSource.saveContacts(it)
                }
        }

        return localContactsDataSource.getContacts()
    }

    override fun getFilteredContacts(input: CharSequence): Single<List<Contact>> =
        localContactsDataSource.getContactsByNameOrPhone(input)


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
