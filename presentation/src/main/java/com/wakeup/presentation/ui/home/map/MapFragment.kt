package com.wakeup.presentation.ui.home.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.wakeup.presentation.databinding.FragmentMapBinding
import com.wakeup.presentation.model.LocationModel
import com.wakeup.presentation.ui.home.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private val viewModel: HomeViewModel by viewModels({ requireParentFragment() })

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mapHelper: MapHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMap()
        collectData()
    }

    private fun initMap() {
        mapHelper = MapHelper(requireContext())
        mapHelper.initMap(childFragmentManager, this)
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE).apply {
                // 사용자가 바라보는 방향으로 지도 회전 활성화
                this.isCompassEnabled = true
            }
    }

    private fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fetchLocationState.collectLatest { state ->
                    if (state) {
                        locationSource.lastLocation?.apply {
                            val location = LocationModel(latitude, longitude)
                            viewModel.setLocation(location)
                        }
                    }
                }
            }
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // 위치 관련 설정
        mapHelper.apply {
            setCurrentLocation(naverMap, locationSource)
            setLocationButtonView(naverMap, binding.lbvLocation)
            setScaleBarView(naverMap, binding.sbvScale)
            setLogoView(naverMap, binding.lvLogo)
        }

        // 테스트 마커 설정
        mapHelper.setTestMarker(naverMap) {
            Snackbar.make(binding.root, "${it.tag}번째 마커", Snackbar.LENGTH_SHORT).show()
            mapHelper.setDarkMode(naverMap)
            true
        }
    }

    // Deprecated 되었지만,
    // Location, Camera Tracking을 위해 Naver의 FusedLocationSource을 사용하려면 어쩔 수 없음
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }

            naverMap.locationTrackingMode = LocationTrackingMode.Follow

            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}