package com.wakeup.presentation.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.wakeup.presentation.R
import com.wakeup.presentation.databinding.FragmentHomeBinding
import com.wakeup.presentation.extension.hideKeyboard
import com.wakeup.presentation.ui.MainActivity
import com.wakeup.presentation.ui.MainViewModel
import com.wakeup.presentation.ui.home.map.MapFragment
import com.wakeup.presentation.ui.home.sheet.BottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var mapFragment: MapFragment
    private lateinit var bottomSheetFragment: BottomSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMap()
        initBottomSheet()
        initLocation()
        initMoments()
        initWeather()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSearchBarListener()
    }

    private fun initMoments() {
        activityViewModel.allMoments?.let { moments ->
            viewModel.initMoments(moments)
            activityViewModel.allMoments = null
        }
    }

    private fun initWeather() {
        viewModel.initWeatherFlow(activityViewModel.weatherState)
    }

    private fun initMap() {
        if (childFragmentManager.findFragmentById(R.id.map) == null) {
            mapFragment = MapFragment()
            childFragmentManager.beginTransaction().add(R.id.map, mapFragment).commit()
        }
    }

    private fun initBottomSheet() {
        if (childFragmentManager.findFragmentById(R.id.bottom_sheet) == null) {
            bottomSheetFragment = BottomSheetFragment()
            childFragmentManager.beginTransaction().add(R.id.bottom_sheet, bottomSheetFragment)
                .commit()
        }
    }

    private fun initLocation() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun hasLocationPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    @OptIn(FlowPreview::class)
    private fun setSearchBarListener() {
        binding.ivMenu.setOnClickListener {
            (activity as MainActivity).openNavDrawer()
        }

        binding.etSearch.setOnEditorActionListener { v, _, _ ->
            v.clearFocus()
            hideKeyboard()
            false
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchQuery.debounce(750).collect {
                    if (::mapFragment.isInitialized.not()) return@collect
                    viewModel.setScrollToTop(true)
                    viewModel.fetchMoments()
                    viewModel.fetchAllMoments()

                    mapFragment.collectMoments()
                    bottomSheetFragment.collectMoments()
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}