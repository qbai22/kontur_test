package com.example.konturtest.data.http

import com.example.konturtest.data.http.dto.DtoContact
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Created by Vladimir Kraev
 */
interface Api {

    @GET("SkbkonturMobile/mobile-test-droid/master/json/generated-01.json")
    fun getContactsFirstSource(): Single<List<DtoContact>>

    @GET("SkbkonturMobile/mobile-test-droid/master/json/generated-02.json")
    fun getContactsSecondSource(): Single<List<DtoContact>>

    @GET("SkbkonturMobile/mobile-test-droid/master/json/generated-03.json")
    fun getContactsThirdSource(): Single<List<DtoContact>>

}