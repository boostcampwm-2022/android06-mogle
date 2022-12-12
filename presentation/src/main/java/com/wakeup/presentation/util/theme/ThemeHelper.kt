package com.wakeup.presentation.util.theme

import android.content.Context
import android.content.res.Configuration
import com.wakeup.domain.model.WeatherType
import com.wakeup.presentation.R
import com.wakeup.presentation.model.WeatherModel
import com.wakeup.presentation.model.WeatherTheme
import com.wakeup.presentation.util.SharedPrefManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Inject

class ThemeHelper @Inject constructor(
    private val context: Context,
) {

    // SharedPrefManager를 필드 주입 받기 위해 필요한 엔트리 포인트.
    // 생성자 주입을 못하는건 아니지만, 엑티비티에서 사용 편의를 위해 필드 주입함.
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SharedPrefEntryPoint {
        fun getSharedPrefEntryPoint(): SharedPrefManager
    }

    // Field Injection from Singleton Component
    private val sharedPrefManager = EntryPointAccessors.fromApplication(context.applicationContext,
        SharedPrefEntryPoint::class.java).getSharedPrefEntryPoint()

    /**
     * 현재 테마 설정 값에 따라, 테마를 직접 초기화하는 함수입니다.
     */
    fun initTheme() {
        val currentTheme = sharedPrefManager.getTheme()
        if (currentTheme == sharedPrefManager.NO_THEME) setThemeBySystemSetting()  // 첫 설치에는 시스템 설정에 따른 테마 설정

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
                sharedPrefManager.saveTheme(WeatherTheme.NIGHT.str)
            }
            else -> {
                sharedPrefManager.saveTheme(WeatherTheme.BRIGHT.str)
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
        sharedPrefManager.saveTheme(theme.str)

        onThemeChanged(theme.str)
    }

    fun getCurrentTheme(): String? = sharedPrefManager.getTheme()
}