package com.wakeup.presentation.util

import android.content.Context
import com.wakeup.presentation.model.WeatherTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val RECREATING_KEY = "ACTIVITY_RECREATE"

    private val AUTO_THEME_KEY = "THEME_SET_BY_AUTOMATICALLY_KEY"

    private val DEFAULT = WeatherTheme.AUTO.str
    private val STORE_KEY = "SharedPreference"
    val THEME_KEY = "THEME"
    val NO_THEME = "NO_THEME"

    val sharedPref = context.getSharedPreferences(STORE_KEY, Context.MODE_PRIVATE)

    fun saveTheme(value: String) {
        with(sharedPref.edit()) {
            putString(THEME_KEY, value)
            apply()
        }
    }

    fun getTheme(): String? {
        return sharedPref.getString(THEME_KEY, DEFAULT)
    }

    fun getThemeByAuto(): String? {
        return sharedPref.getString(AUTO_THEME_KEY, null)
    }

    fun saveThemeByAuto(value: String) {
        with(sharedPref.edit()) {
            putString(AUTO_THEME_KEY, value)
            apply()
        }
    }
}