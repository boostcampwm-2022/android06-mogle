package com.wakeup.presentation.customview.textindicator

import android.animation.ObjectAnimator
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class TextViewPagerCallback(
    private val viewPager: ViewPager2,
    private val indicatorView: TextView,
    private val lifecycleCoroutineScope: LifecycleCoroutineScope,
) : OnPageChangeCallback() {

    private val totalSize by lazy {
        viewPager.adapter?.itemCount
    }

    private val pageChangedFlow = MutableStateFlow(0)
    private var isDragging = false

    var currentPage = 0
    var infiniteCount = 0

    private var indicatorAnimator: ObjectAnimator =
        ObjectAnimator.ofFloat(indicatorView, ALPHA_PROPERTY, START_ALPHA, END_ALPHA)
            .apply {
                duration = FADE_AWAY_DURATION
            }

    init {
        collectPageChangedFlow()
    }

    override fun onPageScrollStateChanged(state: Int) {
        super.onPageScrollStateChanged(state)
        when (state) {
            ViewPager2.SCROLL_STATE_DRAGGING -> isDragging = true
            ViewPager2.SCROLL_STATE_IDLE -> {
                isDragging = false
                currentPage = viewPager.currentItem // 완전히 페이지 변경 되었을 때, 현재 페이지 변경

                // 완료 데이터 방출
                // stateFlow는 이전과 같은 값을 방출하면, collect를 하지 못하여, 계속 값을 증가하여 바꿔줌
                // (오버 플로우가 발생해도, 값이 달라지는 거라 괜찮다고 생각)
                lifecycleCoroutineScope.launch {
                    pageChangedFlow.emit(++infiniteCount)
                }
            }
        }
    }

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int,
    ) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels)

        // 화면 반 이상 스크롤 시 다음 페이지로 숫자 변경
        if (isMoveToRight(currentPage, position)) {
            if (positionOffset > HALF) {
                fadeInIndicator()
                setIndicatorText(position + OFFSET + 1)
            } else {
                setIndicatorText(position + OFFSET)
            }
            return
        }

        // Move to Left
        if (positionOffset > HALF) {
            setIndicatorText(position + OFFSET + 1)
        } else {
            fadeInIndicator()
            setIndicatorText(position + OFFSET)
        }
    }

    private fun isMoveToRight(currentPage: Int, position: Int): Boolean = (currentPage == position)

    private fun fadeInIndicator() {
        indicatorAnimator.cancel()
        indicatorView.alpha = START_ALPHA
    }

    private fun fadeAwayIndicator() {
        if (indicatorView.alpha == END_ALPHA) return
        indicatorAnimator.start()
    }

    private fun setIndicatorText(position: Int) {
        indicatorView.text = TEXT_INDICATOR.format(position, totalSize)
    }

    @OptIn(FlowPreview::class)
    private fun collectPageChangedFlow() {
        // 페이지를 연속으로 변경하는 경우는 무시
        // 페이지 변경 후, 일정 시간 동안 가만히 있어야 사라짐
        lifecycleCoroutineScope.launch {
            pageChangedFlow.debounce(ANIMATION_START_DELAY).collectLatest {
                if (!isDragging) fadeAwayIndicator()
            }
        }
    }

    private companion object {
        const val ALPHA_PROPERTY = "alpha"
        const val START_ALPHA = 1.0f
        const val END_ALPHA = 0.0f
        const val HALF = 0.5
        const val OFFSET = 1 // 인디케이터 1부터 시작 위함
        const val FADE_AWAY_DURATION = 300L
        const val ANIMATION_START_DELAY = 4000L
        const val TEXT_INDICATOR = "%d/%d"
    }
}