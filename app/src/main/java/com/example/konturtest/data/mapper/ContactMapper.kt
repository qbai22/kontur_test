package com.example.konturtest.data.mapper

import com.example.konturtest.data.database.entity.Contact
import com.example.konturtest.data.http.dto.DtoContact

/**
 * Created by Vladimir Kraev
 */
interface ContactMapper {

    fun mapContacts(dtoContacts: List<DtoContact>): List<Contact>

}