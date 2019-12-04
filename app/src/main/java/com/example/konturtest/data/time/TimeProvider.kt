package com.example.konturtest.data.time

/**
 * Created by Vladimir Kraev
 */
interface TimeProvider {

    fun isFirstLoad(): Boolean

    fun isTimeForReload(): Boolean

    fun saveLastLoadTime(timeInMillis: Long)



}