package xunison.com.smarthomeapp.repository

import android.annotation.SuppressLint
import com.example.konturtest.database.ContactsDatabase
import com.example.konturtest.database.entity.Contact
import com.example.konturtest.http.ApiCreator
import com.example.konturtest.http.dto.DtoContact
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers

/**
 * Created by Vladimir Kraev
 */
class ContactsRepository(
    private val apiCreator: ApiCreator,
    private val contactsDatabase: ContactsDatabase
) {

    fun getLocalContacts(): List<Contact> = contactsDatabase.contactsDao().getAllContacts()

    @SuppressLint("CheckResult")
    fun loadContacts(): Single<List<Contact>> {

        val api = apiCreator.getApi()
        val dao = contactsDatabase.contactsDao()

        val firstRequest =
            api.getContactsFirstSource().map { mapDtoContacts(it) }.subscribeOn(Schedulers.io())
        val secondRequest =
            api.getContactsSecondSource().map { mapDtoContacts(it) }.subscribeOn(Schedulers.io())
        val thirdRequest =
            api.getContactsThirdSource().map { mapDtoContacts(it) }.subscribeOn(Schedulers.io())

        return Single.zip(
            firstRequest,
            secondRequest,
            thirdRequest,
            Function3 { l1: List<Contact>,
                        l2: List<Contact>,
                        l3: List<Contact> ->

                ArrayList<Contact>().also {
                    it.addAll(l1)
                    it.addAll(l2)
                    it.addAll(l3)
                    dao.insertAll(it)
                }
            }
        )

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
    }

}
