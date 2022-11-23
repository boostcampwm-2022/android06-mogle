package com.wakeup.presentation.ui.addmoment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.wakeup.presentation.R
import com.wakeup.presentation.databinding.FragmentPlaceCheckBinding
import com.wakeup.presentation.lib.MogleDialog


class PlaceCheckFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentPlaceCheckBinding
    private val args: PlaceCheckFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceCheckBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMap()
        initDialog()
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

        mapFragment.getMapAsync(this)
    }

    private fun initMaker(naverMap: NaverMap) {
        Marker().apply {
            position = LatLng(args.place.location.latitude, args.place.location.longitude)
            isHideCollidedSymbols = true
            isIconPerspectiveEnabled = true
            map = naverMap
        }
    }

    private fun initCameraUpdate(naverMap: NaverMap) {
        val cameraUpdate = CameraUpdate
            .scrollTo(LatLng(args.place.location.latitude, args.place.location.longitude))

        naverMap.moveCamera(cameraUpdate)
    }

    private fun initDialog() {
        MogleDialog
            .with(requireContext(), R.layout.dialog_place)
            .setPlace(args.place)
            .setMargin(0, 0, 0, 100)
            .setGravity(Gravity.BOTTOM)
            .setBackgroundTransParent(true)
            .setCanceledOnTouchOutside(false)
            .setOnPositive(R.id.tv_positive) {
                findNavController().navigate(
                    PlaceCheckFragmentDirections.actionPlaceCheckToAddMoment()
                )
            }
            .setOnNegative(R.id.tv_negative) {
                findNavController().navigateUp()
            }
            .show()
    }

    override fun onMapReady(naverMap: NaverMap) {
        initCameraUpdate(naverMap)
        initMaker(naverMap)
    }
}
