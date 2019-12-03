package com.example.konturtest.data

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by Vladimir Kraev
 */
class TimePreferences(context: Context) : TimeProvider {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun saveLastLoadTime(timeInMillis: Long) {
        preferences.edit().putLong(LOGIN_TIME_KEY, timeInMillis).apply()
    }

    override fun isFirstLoad(): Boolean = getLastLoadTime() == EMPTY_LOAD_TIME

    override fun isTimeForReload(): Boolean {
        val lastLoadTime = getLastLoadTime()
        val currentTime = System.currentTimeMillis()

        return lastLoadTime != EMPTY_LOAD_TIME &&
                currentTime - lastLoadTime > RELOAD_TIME_IN_MILLIS
    }

    private fun getLastLoadTime(): Long = preferences.getLong(LOGIN_TIME_KEY, EMPTY_LOAD_TIME)

    companion object {
        private const val TAG = "TIME_PREF"
        private const val LOGIN_TIME_KEY = "LOGIN_TIME_KEY"
        const val EMPTY_LOAD_TIME = 0L
        private const val RELOAD_TIME_IN_MILLIS = 60000
    }


}