package com.wakeup.presentation.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.wakeup.presentation.R
import com.wakeup.presentation.databinding.FragmentHomeBinding
import com.wakeup.presentation.extension.hideKeyboard
import com.wakeup.presentation.extension.resetStatusBarTransparent
import com.wakeup.presentation.extension.setStatusBarTransparent
import com.wakeup.presentation.model.LocationModel
import com.wakeup.presentation.ui.MainActivity
import com.wakeup.presentation.ui.MainViewModel
import com.wakeup.presentation.ui.home.map.MapFragment
import com.wakeup.presentation.ui.home.sheet.BottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint

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

        setStatusBarTransparent()
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
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    private fun setSearchBarListener() {
        binding.ivMenu.setOnClickListener {
            (activity as MainActivity).openNavDrawer()
        }

        binding.etSearch.setOnEditorActionListener { textView, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setScrollToTop(true)
                viewModel.setSearchQuery(textView.text.toString())
                viewModel.fetchMoments()
                viewModel.fetchAllMoments()

                mapFragment.collectMoments()
                bottomSheetFragment.collectMoments()

                hideKeyboard()
            }
            false // true: 계속 search 가능
        }
    }

    override fun onDestroyView() {
        resetStatusBarTransparent()
        binding.unbind()
        super.onDestroyView()
    }
}