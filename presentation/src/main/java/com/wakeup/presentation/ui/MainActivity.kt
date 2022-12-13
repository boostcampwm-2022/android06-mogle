package com.wakeup.presentation.ui

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.wakeup.presentation.R
import com.wakeup.presentation.databinding.ActivityMainBinding
import com.wakeup.presentation.extension.hideKeyboard
import com.wakeup.presentation.extension.showSnackBar
import com.wakeup.presentation.model.LocationModel
import com.wakeup.presentation.model.WeatherTheme
import com.wakeup.presentation.util.SharedPrefManager
import com.wakeup.presentation.util.theme.ThemeHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private var isUserAction = false
    private lateinit var themeHelper: ThemeHelper

    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        sharedPrefManager = SharedPrefManager(this)
        initLocationPermission()
        initLocation()
        fetchWeatherDataPeriodically()

        initTheme()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        waitMomentDataLoaded()
        setSplashScreenAnimation()
        setBottomNav()
        setSpinner()
    }

    private fun initLocation() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
    }

    private fun initTheme() {
        Timber.d("initTheme")
        themeHelper = ThemeHelper(this, window)
        themeHelper.initTheme(viewModel.weatherState, lifecycleScope)
    }

    fun openNavDrawer() {
        binding.drawerLayout.open()
    }

    private fun waitMomentDataLoaded() {
        binding.root.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                return if (viewModel.isMomentReady.value) {
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

    private fun fetchWeatherDataPeriodically() {
        lifecycleScope.launch {
            viewModel.permissionState.collectLatest { hasPermission ->
                Timber.d("permission $hasPermission")
                while (hasPermission) {
                    getLastLocation { result ->
                        if (result == null) return@getLastLocation

                        launch {
                            viewModel.fetchWeather(
                                LocationModel(
                                    result.latitude,
                                    result.longitude
                                )
                            )
                        }
                    }

                    delay(TEN_MINUTE)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(onSuccessCallback: OnSuccessListener<Location>) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(onSuccessCallback)
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
            if (isTopDest) binding.fab.show() else binding.fab.hide()
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

        binding.layoutDrawer.spinnerTheme.setSelection(
            adapter.getPosition(themeHelper.getCurrentTheme())
        ) // 스피너 설정 값 복원

        // recreate() 후에, 무한 루프 방지
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
                            if (hasLocationPermissions().not()) {
                                binding.root.showSnackBar("위치 권한을 설정해주세요.")
                                return
                            }
                            themeHelper.changeThemeSetting(WeatherTheme.AUTO) {
                                recreate()
                            }
                        }

                        WeatherTheme.BRIGHT.str -> {
                            themeHelper.changeThemeSetting(WeatherTheme.BRIGHT) {
                                recreate()
                            }
                        }

                        WeatherTheme.NIGHT.str -> {
                            themeHelper.changeThemeSetting(WeatherTheme.NIGHT) {
                                recreate()
                            }
                        }

                        WeatherTheme.CLOUDY.str -> {
                            themeHelper.changeThemeSetting(WeatherTheme.CLOUDY) {
                                recreate()
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun initLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.permissionState.value = false
            return
        }

        viewModel.permissionState.value = true
    }

    private fun hasLocationPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.home_fragment) {
            if (ev?.action == MotionEvent.ACTION_DOWN) {
                val v = currentFocus
                if (v is EditText) {
                    val outRect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        v.clearFocus()
                        hideKeyboard(v)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private companion object {
        const val EXIT_ANIM_DURATION = 2000L
        const val ONE_MINUTE = 60000L
        const val TEN_MINUTE = ONE_MINUTE * 10
    }
}