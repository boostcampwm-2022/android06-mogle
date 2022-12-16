package com.wakeup.presentation.ui.home.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay.OnClickListener
import com.naver.maps.map.util.FusedLocationSource
import com.wakeup.presentation.databinding.FragmentMapBinding
import com.wakeup.presentation.extension.getFadeInAnimator
import com.wakeup.presentation.model.LocationModel
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.ui.MainViewModel
import com.wakeup.presentation.ui.home.HomeFragmentDirections
import com.wakeup.presentation.ui.home.HomeViewModel
import com.wakeup.presentation.util.MOVE_CAMERA_KEY
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding

    private val activityViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels({ requireParentFragment() })

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mapHelper: MapHelper

    private val currentMarkers = mutableListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMomentPreviewClickListener()
        initMap()
        collectLocation()
    }

    // 프리뷰 터치시, 상세 페이지 이동
    private fun initMomentPreviewClickListener() {
        binding.momentPreview.root.setOnClickListener {
            binding.momentPreview.momentModel?.let { moment ->
                val action =
                    HomeFragmentDirections.actionMapFragmentToMomentDetailFragment(moment.id)
                findNavController().navigate(action)
            }
        }
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

    private fun collectLocation() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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

    fun collectMoments() {
        // 전체 모먼트 불러오기
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allMoments.collectLatest { moments ->
                    mapHelper.resetMarkers(currentMarkers)
                    currentMarkers.clear()
                    moments.forEach { momentModel ->
                        initMarker(momentModel)
                    }
                }
            }
        }
    }

    private fun moveCameraToAddedLocation() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<LocationModel>(
            MOVE_CAMERA_KEY
        )?.observe(viewLifecycleOwner) { location ->
            mapHelper.moveCamera(naverMap, LatLng(location.latitude, location.longitude))
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        binding.flMapViewContainer.isVisible = true

        // 지도 터치시, 정보 창 사라짐 설정
        mapHelper.setViewFadeOutClickListener(
            naverMap,
            binding.momentPreview.root,
            MOMENT_PREVIEW_ANIM_DURATION
        )

        // 위치 관련 설정
        mapHelper.apply {
            setCurrentLocation(naverMap, locationSource)
            setLocationButtonView(naverMap, binding.lbvLocation)
            setScaleBarView(naverMap, binding.sbvScale)
            setLogoView(naverMap, binding.lvLogo)
        }


        // 모먼트가 추가되면 해당 장소로 이동
        moveCameraToAddedLocation()

        collectMoments()

        // Paging 있이, Moment 가져오기
        /*viewLifecycleOwner.lifecycleScope.launch {
            momentAdapter.loadStateFlow.collectLatest {
                momentAdapter.snapshot().items.forEach { moment ->
                    setMarker(moment)
                }
            }
        }*/

        // 테스트 마커 설정
        /* mapHelper.setTestMarker(naverMap) {
             Snackbar.make(binding.root, "${it.tag}번째 마커", Snackbar.LENGTH_SHORT).show()
             mapHelper.setDarkMode(naverMap)
             true
         } */
    }

    private fun initMarker(moment: MomentModel) {
        val clickListener = OnClickListener { marker ->

            (marker as Marker).apply {
                if (mapHelper.isMarkerFocused(marker)) return@OnClickListener true
                if (mapHelper.checkFocusedMarkerExists()) mapHelper.setMarkerUnfocused()
                mapHelper.setMarkerFocused(this)
                mapHelper.moveCamera(naverMap, position)
                binding.momentModel = (tag as MomentModel)
            }

            binding.momentPreview.root.apply {
                isVisible = true
                getFadeInAnimator(MOMENT_PREVIEW_ANIM_DURATION).start()
            }
            true
        }

        mapHelper.setMomentMarker(naverMap, moment, clickListener) { marker ->
            // 현재 지도에 추가된 마커 관리
            currentMarkers.add(marker)
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

            // permission 변경 이벤트 전파
            activityViewModel.permissionState.value = true // 허용

            naverMap.locationTrackingMode = LocationTrackingMode.Follow

            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroyView() {
        binding.flMapViewContainer.isVisible = false
        binding.unbind()
        super.onDestroyView()
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        const val MOMENT_PREVIEW_ANIM_DURATION = 300L
    }
}