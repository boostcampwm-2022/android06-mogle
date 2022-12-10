package com.wakeup.presentation.util.theme

import android.content.Context
import android.content.res.Configuration
import com.wakeup.presentation.R
import com.wakeup.presentation.model.WeatherTheme
import com.wakeup.presentation.util.SharedPrefManager
import com.wakeup.presentation.util.SharedPrefManager.KEY_THEME

class ThemeHelper(private val context: Context) {

    fun initTheme() {
        val currentTheme = SharedPrefManager.getTheme(context, KEY_THEME)
        if (currentTheme == SharedPrefManager.NO_THEME) setThemeBySystemSetting()  // 첫 설치에는 시스템 설정에 따른 테마 설정

        when (currentTheme) {
            WeatherTheme.BRIGHT.str -> context.setTheme(R.style.Theme_Mogle_Bright)
            WeatherTheme.NIGHT.str -> context.setTheme(R.style.Theme_Mogle_Night)
            WeatherTheme.CLOUDY.str -> context.setTheme(R.style.Theme_Mogle_Cloudy)
        }
    }

    private fun setThemeBySystemSetting() {
        when (context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                SharedPrefManager.saveTheme(context,
                    KEY_THEME,
                    WeatherTheme.NIGHT.str)
            }
            else -> {
                SharedPrefManager.saveTheme(context, KEY_THEME, WeatherTheme.BRIGHT.str)
            }
        }

        // 테마 재설정
        initTheme()
    }

    fun changeTheme(theme: WeatherTheme, onThemeChanged: () -> Unit) {
        SharedPrefManager.saveTheme(
            context,
            KEY_THEME,
            theme.str
        )

        onThemeChanged()
    }

    fun getCurrentTheme(): String? = SharedPrefManager.getTheme(context, KEY_THEME)
}