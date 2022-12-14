package com.wakeup.presentation.ui.home.map

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
import com.wakeup.presentation.model.PictureModel
import com.wakeup.presentation.model.WeatherTheme
import com.wakeup.presentation.util.theme.ThemeHelper

class MapHelper(private val context: Context) {
    /**
     * 현재 포커싱 된 마커 객체
     */
    private var markerFocused: Marker? = null
    private var zIndexBeforeFocused = 0

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

        val setDark = {
            options.apply {
                mapType(NaverMap.MapType.Navi)
                nightModeEnabled(true)
            }
        }

        // 다크 모드 체크
        val themeHelper = ThemeHelper(context)
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

        // 지도 생성
        val mapFragment = MapFragment.newInstance(options).also {
            fm.beginTransaction().replace(R.id.fl_map, it).commit()
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

    /**
     * 인자로 넘어온 마커를 focus 마커로 설정합니다.
     *
     * @param marker 마커 객체
     */
    fun setMarkerFocused(marker: Marker) {
        markerFocused = marker

        markerFocused?.apply {
            zIndexBeforeFocused = zIndex
            width += (width * MARKER_SCALE_UP_SIZE).toInt()
            height += (height * MARKER_SCALE_UP_SIZE).toInt()
            zIndex = Z_INDEX_FRONT
        }
    }

    /**
     * 현재 focus 된 마커를 해제시킵니다.
     *
     */
    fun setMarkerUnfocused() {
        markerFocused?.apply {
            width = MARKER_WIDTH.dp.toInt()
            height = MARKER_HEIGHT.dp.toInt()
            zIndex = zIndexBeforeFocused
        }
        markerFocused = null
    }

    /**
     * 인자로 넘겨진 마커가 현재 포커싱되었는지 확인합니다.
     *
     * @param marker 마커 객체
     * @return true-포커싱 상태
     */
    fun isMarkerFocused(marker: Marker): Boolean {
        return marker === markerFocused
    }

    /**
     * 현재 포커싱된 마커가 있는지 확인합니다.
     *
     * @return true-포커싱된 마커 있음
     */
    fun checkFocusedMarkerExists(): Boolean {
        return markerFocused != null
    }

    /**
     * 지도에 사용자의 위치 정보를 설정합니다.
     *
     * @param map 지도 객체
     * @param _locationSource 사용자의 위치 정보를 제공할 구현체
     */
    fun setCurrentLocation(map: NaverMap, _locationSource: FusedLocationSource) {
        map.apply {
            locationSource = _locationSource
            locationTrackingMode = LocationTrackingMode.NoFollow
        }
    }

    /**
     * 지도에 위치 트래킹 버튼을 설정합니다.
     *
     * @param map 지도 객체
     * @param locationButtonView 위치 트래킹 버튼 뷰
     */
    fun setLocationButtonView(map: NaverMap, locationButtonView: LocationButtonView) {
        locationButtonView.map = map
    }

    /**
     * 지도에 축척 바를 설정합니다.
     *
     * @param map 지도 객체
     * @param scaleBarView 축척 바 뷰
     */
    fun setScaleBarView(map: NaverMap, scaleBarView: ScaleBarView) {
        scaleBarView.map = map
    }

    /**
     * 지도에 네이버 로고를 설정합니다.
     *
     * @param map 지도 객체
     * @param logoView 네이버 로고 뷰
     */
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

    /**
     * 지도에 모먼트 마커를 표시합니다.
     *
     * @param _map 지도 객체
     * @param momentModel 모먼트 데이터
     * @param clickListener 마커 클릭 리스너
     * @param onMarkerAddedCallback 마커 추가 완료시 호출하는 콜백
     */
    fun setMomentMarker(
        _map: NaverMap,
        momentModel: MomentModel,
        clickListener: OnClickListener,
        onMarkerAddedCallback: (Marker) -> Unit,
    ) {
        val markerBinding =
            ItemMapMarkerBinding.inflate(LayoutInflater.from(context), null, false)

        val setMarker = { moment: MomentModel, view: View ->
            val newMarker = Marker().apply {
                width = MARKER_WIDTH.dp.toInt()
                height = MARKER_HEIGHT.dp.toInt()
                anchor = PointF(POINT_X, POINT_Y)
                position = LatLng(
                    moment.place.location.latitude,
                    moment.place.location.longitude
                )
                isHideCollidedSymbols = true
                isIconPerspectiveEnabled = true
                map = _map
                icon = OverlayImage.fromView(view)
                tag = moment
                onClickListener = clickListener
            }

            onMarkerAddedCallback(newMarker)
        }


        // 이미지가 없으면, path를 파일이 없는 경로로 지정하여,
        // onLoadFailed() 유도 -> 기본 이미지 출력
        val picture = if (momentModel.pictures.isEmpty()) {
            PictureModel(NO_IMAGE)
        } else {
            momentModel.pictures.first()
        }

        Glide.with(context)
            .load("${context.filesDir}/images/${picture.path}")
            .listener(object : RequestListener<Drawable> {
                // 이미지가 애초에 없거나, 이미지가 있는데도 로드 실패시 기본 이미지 출력
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    markerBinding.ivThumbnail.setImageResource(R.drawable.ic_no_image)
                    setMarker(momentModel, markerBinding.root)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    markerBinding.ivThumbnail.setImageDrawable(resource)
                    setMarker(momentModel, markerBinding.root)
                    return true
                }
            })
            .into(markerBinding.ivThumbnail)
    }

    /**
     * 인자로 넘어온 마커를 지도에서 삭제 시킵니다.
     * @param markers 마커 객체 리스트
     */
    fun resetMarkers(markers: List<Marker>) {
        markers.forEach { marker ->
            marker.map = null
        }
    }

    /**
     * 인자로 넘어온 위치로 지도 시점을 이동시킵니다.
     *
     * @param map 지도 객체
     * @param position 위도, 경도 위치
     */
    fun moveCamera(map: NaverMap, position: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(position)
            .animate(CameraAnimation.Easing, CAMERA_MOVE_DURATION)
        map.moveCamera(cameraUpdate)
    }

    /**
     * 지도에 테스트 마커를 띄웁니다.
     *
     * @param _map 지도 객체
     * @param clickListener 마커 클릭 리스너
     */
    fun setTestMarker(_map: NaverMap, clickListener: OnClickListener) {
        val markerBinding =
            ItemMapMarkerBinding.inflate(LayoutInflater.from(context), null, false)
        repeat(10) {
            Marker().apply {
                width = MARKER_WIDTH.dp.toInt()
                height = MARKER_HEIGHT.dp.toInt()
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

    private companion object {
        const val INITIAL_LAT = 35.95
        const val INITIAL_LONG = 128.25
        const val MIN_ZOOM = 5.0
        const val CAMERA_MOVE_DURATION = 2000L

        const val INVISIBLE_ALPHA = 0.0f

        const val MARKER_WIDTH = 52
        const val MARKER_HEIGHT = 58
        const val MARKER_SCALE_UP_SIZE = 0.2
        const val Z_INDEX_FRONT = 10
        const val POINT_X = 0.5f
        const val POINT_Y = 0.95f

        const val NO_IMAGE = "NULL_PATH"
    }
}