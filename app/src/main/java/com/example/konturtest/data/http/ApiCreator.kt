package com.example.konturtest.data.http

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Vladimir Kraev
 */

class ApiCreator {

    private var api: Api? = null

    fun getApi(): Api {

        if (api != null) return api!!

        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(Api::class.java)

        return api!!
    }

    companion object {
        private const val BASE_URL = "https://raw.githubusercontent.com/"
        private const val TAG = "API_CREATOR"
    }

}