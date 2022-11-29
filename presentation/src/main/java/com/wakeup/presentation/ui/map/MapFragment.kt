package com.wakeup.presentation.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.wakeup.domain.model.SortType
import com.wakeup.presentation.R
import com.wakeup.presentation.databinding.FragmentMapBinding
import com.wakeup.presentation.extension.getNavigationResultFromTop
import com.wakeup.presentation.extension.hideKeyboard
import com.wakeup.presentation.model.LocationModel
import com.wakeup.presentation.ui.MainActivity
import com.wakeup.presentation.ui.map.sheet.BottomSheetFragment
import com.wakeup.presentation.ui.map.sheet.LocationListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback, LocationListener {
    private lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by viewModels()

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mapHelper: MapHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomSheet()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMap()
        setSearchBarListener()

        updateMoments()
    }

    private fun initBottomSheet() {
        val bottomSheetFragment = BottomSheetFragment()
        childFragmentManager.beginTransaction().add(R.id.bottom_sheet, bottomSheetFragment).commit()
    }

    private fun setSearchBarListener() {
        binding.ivMenu.setOnClickListener {
            (activity as MainActivity).openNavDrawer()
        }

        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setScrollToTop(true)
                viewModel.setSearchQuery(textView.text.toString())
                viewModel.fetchMoments()

                hideKeyboard()
            }
            false // true: 계속 search 가능
        }
    }

    private fun updateMoments() {
        findNavController().getNavigationResultFromTop<Boolean>("isUpdated")
            ?.observe(viewLifecycleOwner) { isUpdated ->
                viewModel.setScrollToTop(isUpdated)
                if (isUpdated) {
                    viewModel.sortType.value = SortType.MOST_RECENT
                    viewModel.fetchMoments()
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

    override fun onSetLocation() {
        locationSource.lastLocation?.apply {
            viewModel.location.value = LocationModel(latitude, longitude)
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

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}