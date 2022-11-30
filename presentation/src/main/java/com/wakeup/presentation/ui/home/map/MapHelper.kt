package com.wakeup.presentation.ui.home.map

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay.OnClickListener
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.widget.LocationButtonView
import com.naver.maps.map.widget.LogoView
import com.naver.maps.map.widget.ScaleBarView
import com.wakeup.presentation.R
import com.wakeup.presentation.databinding.ItemMapMarkerBinding
import com.wakeup.presentation.model.MomentModel

class MapHelper(private val context: Context) {

    /**
     * 지도 생성 함수
     * @param fm 지도 프래그먼트를 위한 프래그먼트 매니저
     * @param callback map 생성 완료시 호출될 콜백
     */
    fun initMap(fm: FragmentManager, callback: OnMapReadyCallback) {
        // 지도 옵션
        val options = NaverMapOptions()
            .camera(CameraPosition(LatLng(INITIAL_LAT, INITIAL_LONG), MIN_ZOOM))
            .minZoom(MIN_ZOOM)
            .locationButtonEnabled(true)
            .tiltGesturesEnabled(true)
            .indoorEnabled(true)
            .zoomControlEnabled(false)
            .logoClickEnabled(true)


        // 지도 생성
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance(options).also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(callback)
    }

    fun setCurrentLocation(map: NaverMap, _locationSource: FusedLocationSource) {
        map.apply {
            locationSource = _locationSource
            locationTrackingMode = LocationTrackingMode.Follow
        }
    }

    fun setLocationButtonView(map: NaverMap, locationButtonView: LocationButtonView) {
        locationButtonView.map = map
    }

    fun setScaleBarView(map: NaverMap, scaleBarView: ScaleBarView) {
        scaleBarView.map = map
    }

    fun setLogoView(map: NaverMap, logoView: LogoView) {
        logoView.setMap(map)
    }

    /**
     * 다크모드 설정
     * @param map 지도 객체
     */
    fun setDarkMode(map: NaverMap) {
        map.apply {
            mapType = NaverMap.MapType.Navi
            isNightModeEnabled = true
        }
    }

    fun setMarker(map: NaverMap, momentModel: MomentModel) {

    }

    fun setTestMarker(_map: NaverMap, clickListener: OnClickListener) {
        val binding = ItemMapMarkerBinding.inflate(LayoutInflater.from(context), null, false)
        binding.ivThumbnail.setImageResource(R.drawable.sample_image)

        repeat(10) {
            Marker().apply {
                position = LatLng(37.5670135 + it * 0.00001, 126.9783740)
                isHideCollidedSymbols = true
                isIconPerspectiveEnabled = true
                map = _map
                icon = OverlayImage.fromView(binding.root)
                tag = "$it"
                onClickListener = clickListener
            }
        }
    }

    companion object {
        const val INITIAL_LAT = 35.95
        const val INITIAL_LONG = 128.25
        const val MIN_ZOOM = 5.0
    }
}