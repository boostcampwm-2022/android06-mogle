package com.wakeup.presentation.ui

import android.animation.ObjectAnimator
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.wakeup.presentation.R
import com.wakeup.presentation.databinding.ActivityMainBinding
import com.wakeup.presentation.model.WeatherTheme
import com.wakeup.presentation.util.SharedPreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private var isUserAction = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        initTheme()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSplashScreenAnimation()
        setBottomNav()
        setSpinner()
        loadData()
    }

    private fun initTheme() {
        val theme = SharedPreferenceManager.getInt(this, KEY_THEME)
        if (theme == NO_THEME) setThemeBySystemSetting()  // 첫 설치에는 시스템 설정에 따른 테마 설정

        when (theme) {
            R.style.Theme_Mogle_Bright -> setTheme(R.style.Theme_Mogle_Bright)
            R.style.Theme_Mogle_Night -> setTheme(R.style.Theme_Mogle_Night)
            R.style.Theme_Mogle_Cloudy -> setTheme(R.style.Theme_Mogle_Cloudy)
        }
    }

    private fun setThemeBySystemSetting() {
        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                SharedPreferenceManager.put(this,
                    KEY_THEME,
                    R.style.Theme_Mogle_Night)
            }
            else -> {
                SharedPreferenceManager.put(this, KEY_THEME, R.style.Theme_Mogle_Bright)
            }
        }

        // 테마 재설정
        initTheme()
    }

    fun openNavDrawer() {
        binding.drawerLayout.open()
    }

    private fun loadData() {
        binding.root.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                return if (viewModel.isReady.value) {
                    Timber.d("Success load data")
                    binding.root.viewTreeObserver.removeOnPreDrawListener(this)
                    true
                } else {
                    Timber.d("Loading..")
                    false
                }
            }
        })
    }

    private fun setSplashScreenAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                ObjectAnimator.ofFloat(splashScreenView, View.ALPHA, 1f, 0f).run {
                    interpolator = AnticipateInterpolator()
                    duration = EXIT_ANIM_DURATION
                    doOnEnd { splashScreenView.remove() }
                    start()
                }
            }
        }
    }

    private fun setBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        setTopLevelDestinations(navController)

        binding.fab.setOnClickListener {
            navController.navigate(R.id.add_moment_navigation)
        }
    }

    private fun setSpinner() {
        val adapter = ArrayAdapter(
            this, R.layout.item_menu, listOf(
                WeatherTheme.AUTO.str,
                WeatherTheme.BRIGHT.str,
                WeatherTheme.NIGHT.str,
                WeatherTheme.CLOUDY.str
            )
        )
        binding.layoutDrawer.spinnerTheme.adapter = adapter

        binding.layoutDrawer.spinnerTheme.setOnTouchListener { _, _ ->
            isUserAction = true
            false
        }

        binding.layoutDrawer.spinnerTheme.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                if (isUserAction) {
                    when (parent.getItemAtPosition(position)) {
                        WeatherTheme.AUTO.str -> {

                        }

                        WeatherTheme.BRIGHT.str -> {
                            SharedPreferenceManager.put(
                                this@MainActivity,
                                KEY_THEME,
                                R.style.Theme_Mogle_Bright
                            )
                            recreate()
                        }

                        WeatherTheme.NIGHT.str -> {
                            SharedPreferenceManager.put(
                                this@MainActivity,
                                KEY_THEME,
                                R.style.Theme_Mogle_Night
                            )
                            recreate()
                        }

                        WeatherTheme.CLOUDY.str -> {
                            SharedPreferenceManager.put(
                                this@MainActivity,
                                KEY_THEME,
                                R.style.Theme_Mogle_Cloudy
                            )
                            recreate()
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun setTopLevelDestinations(navController: NavController) {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
                R.id.globe_fragment
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopDest = appBarConfiguration.topLevelDestinations.contains(destination.id)
            binding.bottomAppBar.isVisible = isTopDest
            binding.fab.isVisible = isTopDest
        }
    }

    companion object {
        private const val EXIT_ANIM_DURATION = 2000L
        private const val KEY_THEME = "theme"
        private const val NO_THEME = -1
    }
}