package com.wakeup.presentation.ui.home.map

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.PointF
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
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
import com.wakeup.presentation.extension.dp
import com.wakeup.presentation.extension.getFadeOutAnimator
import com.wakeup.presentation.extension.setListener
import com.wakeup.presentation.model.MomentModel

class MapHelper(context: Context) {
    private val markerBinding =
        ItemMapMarkerBinding.inflate(LayoutInflater.from(context), null, false)

    /**
     * 현재 포커싱 된 마커 객체
     */
    private var markerFocused: Marker? = null

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
            .locationButtonEnabled(false)
            .zoomControlEnabled(false)
            .logoClickEnabled(true)

        // 지도 생성
        val mapFragment = fm.findFragmentById(R.id.fl_map) as MapFragment?
            ?: MapFragment.newInstance(options).also {
                fm.beginTransaction().add(R.id.fl_map, it).commit()
            }

        mapFragment.getMapAsync(callback)
    }

    /**
     * map을 터치하면, 해당 view가 페이드 아웃 애니메이션을 통해 사라진다.
     *
     * @param map 지도 객체
     * @param view 사라지게 할 뷰
     */
    fun setViewFadeOutClickListener(map: NaverMap, view: View, animDuration: Long) {
        val fadeOutAnim = view.getFadeOutAnimator(animDuration)

        map.setOnMapClickListener { _, _ ->
            if (view.alpha == INVISIBLE_ALPHA) return@setOnMapClickListener

            fadeOutAnim.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.isVisible = false
                }
            }).start()

            setMarkerUnfocused()
        }
    }

    fun setMarkerFocused(marker: Marker) {
        markerFocused = marker

        markerFocused?.apply {
            width += (width * MARKER_SCALE_UP_SIZE).toInt()
            height += (height * MARKER_SCALE_UP_SIZE).toInt()
            zIndex = Z_INDEX_FRONT
        }
    }

    fun setMarkerUnfocused() {
        markerFocused?.apply {
            width -= (width * MARKER_SCALE_UP_SIZE).toInt()
            height -= (height * MARKER_SCALE_UP_SIZE).toInt()
            zIndex = Z_INDEX_BACK
        }
        markerFocused = null
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

    fun setMarker(_map: NaverMap, momentModel: MomentModel, clickListener: OnClickListener) {
        momentModel.pictures.takeIf { it.isNotEmpty() }?.let {
            markerBinding.ivThumbnail.setImageBitmap(it.first().bitmap)
        } ?: kotlin.run {
            markerBinding.ivThumbnail.setImageResource(R.drawable.sample_image2)
        }

        Marker().apply {
            width = MARKER_WIDTH.dp.toInt()
            height = MARKER_HEIGHT.dp.toInt()
            anchor = PointF(POINT_X, POINT_Y)
            position =
                LatLng(momentModel.place.location.latitude, momentModel.place.location.longitude)
            isHideCollidedSymbols = true
            isIconPerspectiveEnabled = true
            map = _map
            icon = OverlayImage.fromView(markerBinding.root)
            tag = momentModel
            onClickListener = clickListener
        }
    }

    fun moveCamera(map: NaverMap, position: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(position)
            .animate(CameraAnimation.Easing, CAMERA_MOVE_DURATION)
        map.moveCamera(cameraUpdate)
    }

    fun setTestMarker(_map: NaverMap, clickListener: OnClickListener) {
        markerBinding.ivThumbnail.setImageResource(R.drawable.sample_image)
        repeat(10) {
            Marker().apply {
                position = LatLng(37.5670135 + it * 0.00001, 126.9783740)
                isHideCollidedSymbols = true
                isIconPerspectiveEnabled = true
                map = _map
                icon = OverlayImage.fromView(markerBinding.root)
                tag = "$it"
                onClickListener = clickListener
            }
        }
    }

    companion object {
        const val INITIAL_LAT = 35.95
        const val INITIAL_LONG = 128.25
        const val MIN_ZOOM = 5.0
        const val CAMERA_MOVE_DURATION = 2000L

        const val INVISIBLE_ALPHA = 0.0f

        const val MARKER_WIDTH = 52
        const val MARKER_HEIGHT = 58
        const val MARKER_SCALE_UP_SIZE = 0.2
        const val Z_INDEX_FRONT = 10
        const val Z_INDEX_BACK = 0
        const val POINT_X = 0.5f
        const val POINT_Y = 0.95f
    }
}