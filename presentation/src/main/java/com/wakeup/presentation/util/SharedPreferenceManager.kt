package com.wakeup.presentation.util

import android.content.Context

object SharedPreferenceManager {
    private val STORE_KEY = "SharedPreference"

    fun put(context: Context, key: String, value: Int) {
        val sharedPref = context.getSharedPreferences(STORE_KEY, Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun getInt(context: Context, key: String): Int {
        val sharedPref = context.getSharedPreferences(STORE_KEY, Context.MODE_PRIVATE)
        return sharedPref.getInt(key, -1)
    }
}