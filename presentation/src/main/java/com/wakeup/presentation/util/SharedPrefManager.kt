package com.wakeup.presentation.util

import android.content.Context
import com.wakeup.presentation.model.WeatherTheme

object SharedPrefManager {
    private val DEFAULT = WeatherTheme.AUTO.str
    private const val STORE_KEY = "SharedPreference"
    const val KEY_THEME = "THEME"
    const val NO_THEME = "NO_THEME"

    fun saveTheme(context: Context, key: String, value: String) {
        val sharedPref = context.getSharedPreferences(STORE_KEY, Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getTheme(context: Context, key: String): String? {
        val sharedPref = context.getSharedPreferences(STORE_KEY, Context.MODE_PRIVATE)
        return sharedPref.getString(key, NO_THEME)
    }
}