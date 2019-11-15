package com.example.konturtest.data

import android.content.Context
import android.preference.PreferenceManager
import com.example.konturtest.data.TimeProvider.Companion.EMPTY_LOAD_TIME

/**
 * Created by Vladimir Kraev
 */
class TimePreferences(context: Context) : TimeProvider {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun saveLastLoadTime(timeInMillis: Long) {
        preferences.edit().putLong(LOGIN_TIME_KEY, timeInMillis).apply()
    }

    override fun getLastLoadTime(): Long = preferences.getLong(LOGIN_TIME_KEY, EMPTY_LOAD_TIME)

    companion object {
        private const val TAG = "TIME_PREF"
        private const val LOGIN_TIME_KEY = "LOGIN_TIME_KEY"
    }

}