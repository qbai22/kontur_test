package com.example.konturtest.data.http.mapper

import com.example.konturtest.data.local.room.entity.Contact
import com.example.konturtest.data.http.dto.DtoContact
import java.time.ZonedDateTime

/**
 * Created by Vladimir Kraev
 */
class DefaultContactMapper : ContactMapper {


    override fun mapContacts(dtoContacts: List<DtoContact>): List<Contact> {
        val mappedContacts = ArrayList<Contact>()

        dtoContacts.forEach { mappedContacts.add(mapContact(it)) }

        return mappedContacts
    }

    private fun mapContact(dtoContact: DtoContact): Contact {
        val id = dtoContact.id
        val name = dtoContact.name
        val phone = dtoContact.phone
        val height = dtoContact.height
        val temperament = dtoContact.temper
        val biography = dtoContact.bio
        val startEducation = formatEducationPeriod(dtoContact.edPeriod.start)
        val endEducation = formatEducationPeriod(dtoContact.edPeriod.end)

        return Contact(
            id,
            name,
            phone,
            height,
            biography,
            temperament,
            startEducation,
            endEducation
        )

    }

    private fun formatEducationPeriod(dateString: String): String {

        val zonedDayTime = ZonedDateTime.parse(dateString)

        val year = zonedDayTime.year
        val month = zonedDayTime.month.value
        val day = zonedDayTime.dayOfMonth

        return "${String.format("%02d", day)}.${String.format("%02d", month)}.$year"
    }


}