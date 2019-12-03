package com.example.konturtest.data.http

import com.example.konturtest.data.local.room.entity.Contact
import com.example.konturtest.data.http.api.ApiCreator
import com.example.konturtest.data.http.mapper.ContactMapper
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers

/**
 * Created by Vladimir Kraev
 */

internal class GithubContactsDataSource(
    apiCreator: ApiCreator,
    private val contactMapper: ContactMapper
) : RemoteContactsDataSource {

    private val api = apiCreator.getApi()

    override fun loadContacts(): Single<List<Contact>> {

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


}