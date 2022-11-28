package com.wakeup.presentation.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.wakeup.domain.model.SortType
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.MomentPagingAdapter
import com.wakeup.presentation.databinding.BottomSheetBinding
import com.wakeup.presentation.databinding.FragmentMapBinding
import com.wakeup.presentation.extension.getNavigationResultFromTop
import com.wakeup.presentation.model.LocationModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by viewModels()
    private val momentAdapter = MomentPagingAdapter()

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mapHelper: MapHelper

    private var isUpdated = false

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
        setAdapterListener()

        collectMoments()
        updateMoments()
    }

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            if (isUpdated) {
                binding.bottomSheet.rvMoments.scrollToPosition(0)
            }
            isUpdated = false
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            binding.bottomSheet.rvMoments.scrollToPosition(0)
        }
    }

    private fun setAdapterListener() {
        momentAdapter.registerAdapterDataObserver(adapterDataObserver)

        momentAdapter.addLoadStateListener {
            binding.bottomSheet.hasMoments = momentAdapter.itemCount > 0
        }
    }

    private fun updateMoments() {
        findNavController().getNavigationResultFromTop<Boolean>("isUpdated")
            ?.observe(viewLifecycleOwner) { isUpdated ->
                this.isUpdated = isUpdated
                if (isUpdated) {
                    binding.bottomSheet.sortMenu.setText(R.string.most_recent)
                    viewModel.sortType.value = SortType.MOST_RECENT
                    viewModel.fetchMoments()
                }
            }
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
            setState(this)
            setMenus(this)
            setAdapter(this)
            setCallback(this)
        }
    }

    private fun setState(binding: BottomSheetBinding) {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.state = viewModel.bottomSheetState.value
    }

    private fun setMenus(binding: BottomSheetBinding) {
        val items = listOf(
            getString(R.string.most_recent),
            getString(R.string.oldest),
            getString(R.string.nearest)
        )
        val menuAdapter = ArrayAdapter(requireContext(), R.layout.item_sort_menu, items)
        binding.textField.viewTreeObserver.addOnGlobalLayoutListener {
            (binding.textField.editText as? MaterialAutoCompleteTextView)?.setAdapter(menuAdapter)
        }

        binding.sortMenu.setOnItemClickListener { _, _, position, _ ->
            viewModel.location.value = null
            viewModel.sortType.value = when (position) {
                0 -> SortType.MOST_RECENT
                1 -> SortType.OLDEST
                else -> {
                    locationSource.lastLocation?.apply {
                        viewModel.location.value = LocationModel(latitude, longitude)
                    }
                    SortType.NEAREST
                }
            }
            viewModel.fetchMoments()
        }
    }

    private fun setAdapter(binding: BottomSheetBinding) {
        binding.rvMoments.adapter = momentAdapter
    }

    private fun setCallback(binding: BottomSheetBinding) {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet)
        if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            binding.textField.visibility = View.INVISIBLE
        }

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        changeVisibleMenu(true)
                        viewModel.bottomSheetState.value = BottomSheetBehavior.STATE_EXPANDED
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        changeVisibleMenu(false)
                        viewModel.bottomSheetState.value = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    private fun changeVisibleMenu(isVisible: Boolean) = with(binding.bottomSheet.textField) {
        if (isVisible) {
            visibility = View.VISIBLE
            animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        } else {
            visibility = View.INVISIBLE
            animation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        }
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
        momentAdapter.unregisterAdapterDataObserver(adapterDataObserver)
        binding.unbind()
        super.onDestroyView()
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}