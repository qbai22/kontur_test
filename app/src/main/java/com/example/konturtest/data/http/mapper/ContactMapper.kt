package com.example.konturtest.data.http.mapper

import com.example.konturtest.data.local.room.entity.Contact
import com.example.konturtest.data.http.dto.DtoContact

/**
 * Created by Vladimir Kraev
 */
interface ContactMapper {

    fun mapContacts(dtoContacts: List<DtoContact>): List<Contact>

}