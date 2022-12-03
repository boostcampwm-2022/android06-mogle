package com.wakeup.presentation.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.DetailPictureAdapter
import com.wakeup.presentation.databinding.FragmentMomentDetailBinding
import com.wakeup.presentation.util.setToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MomentDetailFragment : Fragment() {
    private lateinit var binding: FragmentMomentDetailBinding
    private val args: MomentDetailFragmentArgs by navArgs()

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

        initToolbar()
        initMoment()
        initViewPagerAdapter()
    }

    private fun initToolbar() {
        setToolbar(
            toolbar = binding.tbDetailMoment,
            titleId = R.string.moment,
            onBackClick = { findNavController().navigateUp() }
        )
    }

    private fun initMoment() {
        binding.momentModel = args.momentModel
    }

    private fun initViewPagerAdapter() {
        binding.vp2Images.adapter = DetailPictureAdapter()
        binding.tpiIndicator.attachTo(binding.vp2Images, viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}