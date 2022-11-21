package com.wakeup.presentation.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.map
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.wakeup.domain.model.SortType
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.MomentPagingAdapter
import com.wakeup.presentation.databinding.BottomSheetBinding
import com.wakeup.presentation.databinding.FragmentMapBinding
import com.wakeup.presentation.databinding.ItemMapMarkerBinding
import com.wakeup.presentation.model.LocationModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by viewModels()
    private val momentAdapter = MomentPagingAdapter()

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        initLocation()
        initBottomSheet()

        collectMoments()
    }

    private fun collectMoments() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.moments.collectLatest {
                    momentAdapter.submitData(it)
                }
            }
        }
    }

    private fun initBottomSheet() {
        with(binding.bottomSheet) {
            setMenus(this)
            setAdapter(this)
        }
    }

    private fun setMenus(binding: BottomSheetBinding) {
        val items = listOf(
            getString(R.string.date_sort_desc),
            getString(R.string.date_sort_asc),
            getString(R.string.location_sort)
        )
        val menuAdapter = ArrayAdapter(requireContext(), R.layout.item_sort_menu, items)
        (binding.textField.editText as? AutoCompleteTextView)?.setAdapter(menuAdapter)

        binding.sortMenu.setOnItemClickListener { _, _, position, _ ->
            expandBottomSheet(binding.bottomSheet)

            Timber.d("${locationSource.lastLocation?.latitude} ${locationSource.lastLocation?.longitude}")
            when (position) {
                0 -> viewModel.fetchMoments(SortType.MOST_RECENT)
                1 -> viewModel.fetchMoments(SortType.OLDEST)
                else -> locationSource.lastLocation?.apply {
                    viewModel.fetchMoments(
                        sortType = SortType.CLOSET,
                        location = LocationModel(latitude, longitude)
                    )
                }
            }
        }
    }

    private fun expandBottomSheet(bottomSheet: ConstraintLayout) {
        val behavior = BottomSheetBehavior.from(bottomSheet)
        if (behavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setAdapter(binding: BottomSheetBinding) {
        binding.rvMoments.adapter = momentAdapter
    }

    private fun initMap() {
        // TODO Fragment 내부에서는, childFragmentManager 사용 필요
        val fm = childFragmentManager

        // 지도 옵션
        val options = NaverMapOptions()
            .locationButtonEnabled(true)
            .tiltGesturesEnabled(true)
            .indoorEnabled(true)
            .zoomControlEnabled(false)
        //.mapType(NaverMap.MapType.Navi)
        //.nightModeEnabled(true)

        // 지도 생성
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance(options).also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    private fun initLocation() {
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE).apply {
                // 사용자가 바라보는 방향으로 지도 회전 활성화
                this.isCompassEnabled = true
            }
    }

    // TODO 테스트 코드 수정
    private fun setTestMarkers() {
        val markerBinding = ItemMapMarkerBinding.inflate(layoutInflater, binding.map, false)
        markerBinding.ivThumbnail.setImageResource(R.drawable.sample_image)

        repeat(10) {
            Marker().apply {
                this.position = LatLng(37.5670135 + it * 0.00001, 126.9783740)
                this.isHideCollidedSymbols = true
                this.isIconPerspectiveEnabled = true
                this.map = naverMap
                this.icon = OverlayImage.fromView(markerBinding.root)
                this.tag = "$it"
                this.setOnClickListener {
                    Snackbar.make(binding.root, "${it.tag}번째 마커", Snackbar.LENGTH_SHORT).show()
                    true
                }
            }

        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap.also {
            // 위치 설정
            it.locationSource = locationSource
            it.locationTrackingMode = LocationTrackingMode.Follow
        }

        // 테스트 마커
        setTestMarkers()
    }

    // Deprecated 되었지만,
    // Location, Camera Tracking을 위해 Naver의 FusedLocationSource을 사용하려면 어쩔 수 없음
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
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