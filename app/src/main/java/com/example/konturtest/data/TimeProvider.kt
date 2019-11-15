package com.example.konturtest.data

/**
 * Created by Vladimir Kraev
 */
interface TimeProvider {

    fun saveLastLoadTime(timeInMillis: Long)

    fun getLastLoadTime(): Long

    companion object {
        const val EMPTY_LOAD_TIME = 0L
    }
}