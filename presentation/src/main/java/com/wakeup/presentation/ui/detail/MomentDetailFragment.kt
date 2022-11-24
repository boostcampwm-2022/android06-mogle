package com.wakeup.presentation.ui.detail

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.DetailPictureAdapter
import com.wakeup.presentation.databinding.FragmentMomentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MomentDetailFragment : Fragment() {
    private lateinit var binding: FragmentMomentDetailBinding
    private val args: MomentDetailFragmentArgs by navArgs()

    // TODO 역할 위임
    private lateinit var indicatorAnimator: ObjectAnimator
    private val pageChangedFlow = MutableStateFlow(0)
    private var isDragging = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMomentDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAnimator()
        initViewPagerAdapter()
        initMoment()
        collectPageChangedFlow()
    }

    private fun initAnimator() {
        indicatorAnimator =
            ObjectAnimator.ofFloat(binding.tvIndicator, ALPHA_PROPERTY, START_ALPHA, END_ALPHA)
                .apply {
                    duration = FADE_AWAY_DURATION
                }
    }

    private fun initViewPagerAdapter() {
        binding.vp2Images.adapter = DetailPictureAdapter()

        binding.vp2Images.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            var currentPage = 0
            var infiniteCount = 0

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when (state) {
                    SCROLL_STATE_DRAGGING -> isDragging = true
                    SCROLL_STATE_IDLE -> {
                        isDragging = false
                        currentPage = binding.vp2Images.currentItem // 완전히 페이지 변경 되었을 때, 현재 페이지 변경

                        // 완료 데이터 방출
                        // stateFlow는 이전과 같은 값을 방출하면, collect를 하지 못하여, 계속 값을 증가하여 바꿔줌
                        // (오버 플로우가 발생해도, 값이 달라지는 거라 괜찮다고 생각)
                        viewLifecycleOwner.lifecycleScope.launch {
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
                        showIndicator()
                        setIndicator(position + OFFSET + 1)
                    } else setIndicator(position + OFFSET)
                    return
                }

                // Move to Left
                if (positionOffset > HALF) {
                    setIndicator(position + OFFSET + 1)
                } else {
                    showIndicator()
                    setIndicator(position + OFFSET)
                }
            }
        })
    }

    private fun isMoveToRight(currentPage: Int, position: Int): Boolean = (currentPage == position)

    private fun showIndicator() {
        indicatorAnimator.cancel()
        binding.tvIndicator.alpha = START_ALPHA
    }

    private fun setIndicator(position: Int) {
        val totalSize = args.momentModel.pictures.size
        val res = resources
        binding.tvIndicator.text = res.getString(R.string.text_indicator, position, totalSize)
    }

    private fun fadeAwayIndicator() {
        if (binding.tvIndicator.alpha == END_ALPHA) return
        indicatorAnimator.start()
    }

    private fun initMoment() {
        binding.momentModel = args.momentModel
    }

    @OptIn(FlowPreview::class)
    private fun collectPageChangedFlow() {
        // 페이지를 연속으로 변경하는 경우는 무시
        // 페이지 변경 후, 일정 시간 동안 가만히 있어야 사라짐
        viewLifecycleOwner.lifecycleScope.launch {
            pageChangedFlow.debounce(ANIMATION_START_DELAY).collectLatest {
                if (!isDragging) fadeAwayIndicator()
            }
        }
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }

    private companion object {
        // 인디케이터 1부터 시작 위함
        const val ALPHA_PROPERTY = "alpha"
        const val START_ALPHA = 1.0f
        const val END_ALPHA = 0.0f
        const val HALF = 0.5
        const val OFFSET = 1
        const val FADE_AWAY_DURATION = 300L
        const val ANIMATION_START_DELAY = 4000L
    }
}