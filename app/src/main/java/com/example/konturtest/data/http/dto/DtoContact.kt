package com.example.konturtest.data.http.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by Vladimir Kraev
 */
data class DtoContact(

    var id: String,

    var name: String,

    var phone: String,

    var height: Float,

    @SerializedName("biography")
    var bio: String,

    @SerializedName("temperament")
    var temper: String,

    @SerializedName("educationPeriod")
    var edPeriod: EducationPeriod

)