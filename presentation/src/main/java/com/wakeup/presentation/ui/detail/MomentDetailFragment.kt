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
import timber.log.Timber

@AndroidEntryPoint
class MomentDetailFragment : Fragment() {
    private lateinit var binding: FragmentMomentDetailBinding
    private val args: MomentDetailFragmentArgs by navArgs()

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

        initViewPagerAdapter()
        initMoment()
        collectFlow()
    }

    private fun initViewPagerAdapter() {
        binding.vp2Images.adapter = DetailPictureAdapter()

        binding.vp2Images.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            var currentPage = 0

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when (state) {
                    SCROLL_STATE_DRAGGING -> isDragging = true
                    SCROLL_STATE_IDLE -> isDragging = false
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

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position

                Timber.d("Holding onPageSelected")
                viewLifecycleOwner.lifecycleScope.launch {
                    pageChangedFlow.emit(position)
                }
            }
        })
    }

    private fun isMoveToRight(currentPage: Int, position: Int): Boolean = (currentPage == position)

    private fun showIndicator() {
        binding.tvIndicator.alpha = START_ALPHA
    }

    private fun setIndicator(position: Int) {
        val totalSize = args.momentModel.pictures.size
        val res = resources
        binding.tvIndicator.text = res.getString(R.string.text_indicator, position, totalSize)
    }

    private fun fadeAwayIndicator() {
        ObjectAnimator.ofFloat(binding.tvIndicator, ALPHA_PROPERTY, START_ALPHA, END_ALPHA).apply {
            duration = FADE_AWAY_DURATION
        }.start()
    }

    private fun initMoment() {
        binding.momentModel = args.momentModel
    }

    @OptIn(FlowPreview::class)
    private fun collectFlow() {
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

    companion object {
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