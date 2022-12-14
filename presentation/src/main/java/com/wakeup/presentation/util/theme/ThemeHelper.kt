package com.wakeup.presentation.util.theme

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleCoroutineScope
import com.wakeup.domain.model.WeatherType
import com.wakeup.presentation.R
import com.wakeup.presentation.model.WeatherModel
import com.wakeup.presentation.model.WeatherTheme
import com.wakeup.presentation.ui.UiState
import com.wakeup.presentation.util.SharedPrefManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ThemeHelper @Inject constructor(
    private val context: Context,
    private val window: Window? = null
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
    fun initTheme(
        weather: StateFlow<UiState<WeatherModel>>? = null,
        lifecycle: LifecycleCoroutineScope? = null,
    ) {
        Timber.d("ThemeHelper")
        val currentTheme = sharedPrefManager.getTheme()
        val currentAutoTheme = sharedPrefManager.getThemeByAuto()

        if (currentTheme == sharedPrefManager.NO_THEME) setThemeBySystemSetting()  // 테마 설정 값 없다면, 시스템 설정에 따른 테마 설정

        when (currentTheme) {
            // 테마 설정이 [자동]일 때
            WeatherTheme.AUTO.str -> {
                if (currentAutoTheme != null) {
                    setTheme(currentAutoTheme)  // 자동으로 바뀐 테마 값으로 테마 변경
                }

                // 날씨 정보 옵저빙
                lifecycle?.launch {
                    weather?.collectLatest { state ->
                        when (state) {
                            is UiState.Success -> {
                                Timber.d("Theme ${state.item}")
                                changeThemeByWeatherAndTime(state.item) {
                                    (context as? Activity)?.recreate()
                                }
                            }
                            else -> Unit
                        }
                    }
                }
            }

            // [자동] 이외 테마
            else -> setTheme(currentTheme)
        }
    }

    /**
     * 테마 설정
     *
     * @param themeStr
     */
    private fun setTheme(themeStr: String?) {
        when (themeStr) {
            WeatherTheme.BRIGHT.str -> {
                context.setTheme(R.style.Theme_Mogle_Bright)
                if (window == null) return
                window.statusBarColor = ContextCompat.getColor(context, R.color.white)
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                    true
            }
            WeatherTheme.NIGHT.str -> {
                context.setTheme(R.style.Theme_Mogle_Night)
                if (window == null) return
                window.statusBarColor = ContextCompat.getColor(context, R.color.black)
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                    false
            }
            WeatherTheme.CLOUDY.str -> {
                context.setTheme(R.style.Theme_Mogle_Cloudy)
                if (window == null) return
                window.statusBarColor = ContextCompat.getColor(context, R.color.cloudy_dark_gray)
                WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                    false
            }
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

    /**
     * 날씨와 시간에 따라, 즉시 테마를 변경합니다.
     *
     * [테마 변경 조건]
     * 1. 시스템 설정 시간을 기준으로 오전 6시 - 오후 5시 59분까지는 낮입니다. 이외는 밤입니다.
     * 2. 비나 눈이 올 때만, 흐림입니다.
     *
     * @param weather 날씨 정보
     * @param onThemeChanged 테마 변경 후, 호출되는 콜백 - 변경된 테마 이름을 넘길 수 있습니다.
     */
    fun changeThemeByWeatherAndTime(weather: WeatherModel, onThemeChanged: (String) -> Unit) {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        val isNight = currentHour < 6 || currentHour > 17
        val isCloudy = weather.type == WeatherType.RAIN || weather.type == WeatherType.SNOW

        Timber.d("Theme ${sharedPrefManager.getThemeByAuto()}")

        if (isCloudy) {
            if (sharedPrefManager.getThemeByAuto() == WeatherTheme.CLOUDY.str) return

            setTheme(WeatherTheme.CLOUDY.str)
            changeAutoThemeSetting(WeatherTheme.CLOUDY)
            onThemeChanged(WeatherTheme.CLOUDY.str)
            return
        }

        if (isNight) {
            if (sharedPrefManager.getThemeByAuto() == WeatherTheme.NIGHT.str) return

            setTheme(WeatherTheme.NIGHT.str)
            changeAutoThemeSetting(WeatherTheme.NIGHT)
            onThemeChanged(WeatherTheme.NIGHT.str)
            return
        }

        if (sharedPrefManager.getThemeByAuto() == WeatherTheme.BRIGHT.str) return

        setTheme(WeatherTheme.BRIGHT.str)
        changeAutoThemeSetting(WeatherTheme.BRIGHT)
        onThemeChanged(WeatherTheme.BRIGHT.str)
    }

    /**
     * 자동으로 변경된 테마를 저장하고 있는 값을 변경
     *
     * @param theme
     */
    private fun changeAutoThemeSetting(theme: WeatherTheme) {
        sharedPrefManager.saveThemeByAuto(theme.str)
    }

    /**
     * 테마 설정 값만 변경 후, 콜백을 호출합니다.
     */
    fun changeThemeSetting(theme: WeatherTheme, onThemeChanged: (String) -> Unit) {
        sharedPrefManager.saveTheme(theme.str)

        onThemeChanged(theme.str)
    }

    fun getCurrentTheme(): String? = sharedPrefManager.getTheme()

    fun getCurrentThemeByAuto(): String? = sharedPrefManager.getThemeByAuto()
}