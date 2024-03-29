package com.example.konturtest.data

import android.annotation.SuppressLint
import com.example.konturtest.data.http.RemoteContactsDataSource
import com.example.konturtest.data.local.LocalContactsDataSource
import com.example.konturtest.data.local.room.entity.Contact
import com.example.konturtest.data.time.TimeProvider
import io.reactivex.Single

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

    //I wasn't sure should i show cached contacts after 1 min and error on onErrorResumeNext
    //or it will be better to show empty list
    @SuppressLint("CheckResult")
    override fun getContacts(isForceLoad: Boolean): Single<List<Contact>> {

        //Checks did application already load some data before or not
        val isFirstLoad = loadTimeProvider.isFirstLoad()
        val isTimeForReload = loadTimeProvider.isTimeForReload()

        //Last load time updating and cashing contacts only if loading was successful
        if (isFirstLoad || isForceLoad || isTimeForReload) {
            return remoteContactsDataSource.loadContacts()
                .doOnSuccess {
                    loadTimeProvider.saveLastLoadTime(System.currentTimeMillis())
                    localContactsDataSource.saveContacts(it)
                }
        }

        return localContactsDataSource.getContacts()
    }

    override fun getFilteredContacts(input: String): Single<List<Contact>> =
        localContactsDataSource.getFilteredContacts(input)


    companion object {
        private const val TAG = "CONTACTS_REPO"
    }

}
