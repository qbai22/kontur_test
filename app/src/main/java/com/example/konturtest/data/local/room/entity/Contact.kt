package com.example.konturtest.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Vladimir Kraev
 */
@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey
    var id: String,
    var name: String,
    var phone: String,
    var height: Float,
    var bio: String,
    var temperament: String,
    var startEducation: String,
    var endEducation: String
)


/*
id: "5bbb009d0a125490a5685f41",
name: "Margery Jacobson",
phone: "+7 (822) 466-2280",
height: 186.59,
biography: "Aliqua aliqua magna sit velit enim mollit voluptate mollit magna. Aliquip laborum labore eu eu anim ipsum laboris dolore laborum sunt. Elit elit cillum culpa sit ullamco.",
temperament: "phlegmatic",
educationPeriod: {
    start: "2016-04-07T12:48:49-05:00",
    end: "2016-06-30T08:19:35-05:00"
}*/
