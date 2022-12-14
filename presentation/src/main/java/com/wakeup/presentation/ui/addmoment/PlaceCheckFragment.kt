package com.wakeup.presentation.ui.addmoment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.wakeup.presentation.R
import com.wakeup.presentation.databinding.FragmentPlaceCheckBinding
import com.wakeup.presentation.databinding.ItemMapMarkerBinding
import com.wakeup.presentation.model.WeatherTheme
import com.wakeup.presentation.util.setToolbar
import com.wakeup.presentation.util.theme.ThemeHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlaceCheckFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentPlaceCheckBinding
    private val viewModel: PlaceCheckViewModel by viewModels()
    private val args: PlaceCheckFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceCheckBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPlace()
        initMap()
        initDialog()
        initToolbar()

    }

    private fun initPlace() {
        viewModel.setPlace(args.place)
    }

    private fun initMap() {
        val fm = childFragmentManager

        val options = NaverMapOptions()
            .zoomControlEnabled(false)
            .indoorEnabled(true)

        val mapFragment = fm.findFragmentById(R.id.fl_map) as MapFragment?
            ?: MapFragment.newInstance(options).also {
                fm.beginTransaction().add(R.id.fl_map, it).commit()
            }

        val setDark = {
            options.apply {
                mapType(NaverMap.MapType.Navi)
                nightModeEnabled(true)
            }
        }

        // 다크 모드 체크
        val themeHelper = ThemeHelper(requireContext())
        when (themeHelper.getCurrentTheme()) {
            WeatherTheme.AUTO.str -> {
                when (themeHelper.getCurrentThemeByAuto()) {
                    WeatherTheme.NIGHT.str -> {
                        setDark()
                    }
                }
            }
            WeatherTheme.NIGHT.str -> {
                setDark()
            }
        }

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        initCameraUpdate(naverMap)
        initMaker(naverMap)
    }

    private fun initMaker(naverMap: NaverMap) {

        val markerBinding =
            ItemMapMarkerBinding.inflate(LayoutInflater.from(context), null, false).apply {
                ivThumbnail.setImageResource(R.drawable.ic_launcher_mogle_foreground)
            }

        Marker().apply {
            position = LatLng(args.place.location.latitude, args.place.location.longitude)
            isHideCollidedSymbols = true
            isIconPerspectiveEnabled = true
            map = naverMap
            icon = OverlayImage.fromView(
                markerBinding.root
            )
        }
    }

    private fun initCameraUpdate(naverMap: NaverMap) {
        val cameraUpdate = CameraUpdate
            .scrollTo(LatLng(args.place.location.latitude, args.place.location.longitude))

        naverMap.moveCamera(cameraUpdate)
    }

    private fun initDialog() {
        binding.tvPositive.setOnClickListener {
            findNavController().navigate(
                PlaceCheckFragmentDirections.actionPlaceCheckToAddMoment(args.place)
            )
        }

        binding.tvNegative.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initToolbar() {
        setToolbar(
            toolbar = binding.tbPlaceCheck,
            titleId = R.string.place_check,
            onBackClick = { findNavController().navigateUp() }
        )
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}