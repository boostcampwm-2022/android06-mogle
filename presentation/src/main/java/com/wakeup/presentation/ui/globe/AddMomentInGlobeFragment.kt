package com.wakeup.presentation.ui.globe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.MomentPagingAdapter
import com.wakeup.presentation.databinding.FragmentAddMomentInGlobeBinding
import com.wakeup.presentation.extension.dp
import com.wakeup.presentation.util.setToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMomentInGlobeFragment : Fragment() {
    private val viewModel: AddMomentInGlobeViewModel by viewModels()
    private lateinit var binding: FragmentAddMomentInGlobeBinding
    private val momentPagingAdapter = MomentPagingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddMomentInGlobeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
    }

    private fun initToolbar() {
        setToolbar(
            toolbar = binding.tbAddMomentInGlobe,
            titleId = R.string.add_moment_in_globe,
            onBackClick = { findNavController().navigateUp() }
        )
    }

    private fun initAdapter() {
        val largeSpan = 5
        val smallSpan = 3
        val criteriaWidthDp = 500
        binding.rvSaveMomentInGlobe.apply {
            adapter = momentPagingAdapter
            layoutManager = GridLayoutManager(
                requireContext(),
                if (getWidthDp() > criteriaWidthDp) largeSpan else smallSpan
            )
            addItemDecoration(GridSpaceItemDecoration(12.dp))
        }
    }

    private fun getWidthDp(): Float =
        resources.displayMetrics.widthPixels / resources.displayMetrics.density
}