package com.wakeup.presentation.ui.map

import android.graphics.BitmapFactory
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
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.wakeup.domain.model.SortType
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.MomentPagingAdapter
import com.wakeup.presentation.databinding.BottomSheetBinding
import com.wakeup.presentation.databinding.FragmentMapBinding
import com.wakeup.presentation.factory.FakeMomentFactory
import com.wakeup.presentation.model.LocationModel
import com.wakeup.presentation.model.PictureModel
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
    private lateinit var mapHelper: MapHelper

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
        initMapHelper()
        initMap()
        initLocation()
        initBottomSheet()
        initTestButton()

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

    // TODO 추후 삭제
    private fun initTestButton() {
        val bitmap =
            BitmapFactory.decodeResource(requireContext().resources, R.drawable.sample_image2)
        val picture = PictureModel(bitmap)

        binding.btnTest.setOnClickListener {
            val fakeMoment = FakeMomentFactory.createMomentsWithSampleImage(picture, 1)
            val action =
                MapFragmentDirections.actionMapFragmentToMomentDetailFragment(fakeMoment.first())
            findNavController().navigate(action)
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
                        sortType = SortType.NEAREST,
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

    private fun initMapHelper() {
        mapHelper = MapHelper(requireContext())
    }

    private fun initMap() {
        mapHelper.initMap(childFragmentManager, this)
    }

    private fun initLocation() {
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE).apply {
                // 사용자가 바라보는 방향으로 지도 회전 활성화
                this.isCompassEnabled = true
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