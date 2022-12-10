package com.wakeup.presentation.util.theme

import android.content.Context
import android.content.res.Configuration
import com.wakeup.domain.model.WeatherType
import com.wakeup.presentation.R
import com.wakeup.presentation.model.WeatherModel
import com.wakeup.presentation.model.WeatherTheme
import com.wakeup.presentation.util.SharedPrefManager
import com.wakeup.presentation.util.SharedPrefManager.KEY_THEME
import java.util.*

class ThemeHelper(private val context: Context) {

    /**
     * 현재 테마 설정 값에 따라, 테마를 직접 초기화하는 함수입니다.
     */
    fun initTheme() {
        val currentTheme = SharedPrefManager.getTheme(context, KEY_THEME)
        if (currentTheme == SharedPrefManager.NO_THEME) setThemeBySystemSetting()  // 첫 설치에는 시스템 설정에 따른 테마 설정

        when (currentTheme) {
            WeatherTheme.AUTO.str -> {
                // TODO 날씨와 시간에 따라 변경 추가
            }

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

    // TODO 현재는 테마 설정 값 자체를 바꾸고 있어서, 앱 재시작시 해당 테마로 스피너가 설정되어 있음
    // TODO 이후, 로직 변경 예정
    fun changeAutoTheme(weather: WeatherModel, onThemeChanged: (String) -> Unit) {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        val isNight = currentHour < 6 || currentHour > 18
        val isCloudy = weather.type == WeatherType.RAIN || weather.type == WeatherType.CLOUDS

        if (isCloudy) {
            changeThemeSetting(WeatherTheme.CLOUDY, onThemeChanged)
            return
        }

        if (isNight) {
            changeThemeSetting(WeatherTheme.NIGHT, onThemeChanged)
            return
        }

        changeThemeSetting(WeatherTheme.BRIGHT, onThemeChanged)
    }

    /**
     * 테마 설정 값만 변경 후, 콜백을 호출합니다.
     */
    fun changeThemeSetting(theme: WeatherTheme, onThemeChanged: (String) -> Unit) {
        SharedPrefManager.saveTheme(context, KEY_THEME, theme.str)

        onThemeChanged(theme.str)
    }

    fun getCurrentTheme(): String? = SharedPrefManager.getTheme(context, KEY_THEME)
}