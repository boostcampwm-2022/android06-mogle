package com.wakeup.presentation.ui.globe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.wakeup.presentation.adapter.GlobeAdapter
import com.wakeup.presentation.databinding.FragmentGlobeBinding
import com.wakeup.presentation.ui.GridSpaceItemDecoration
import com.wakeup.presentation.ui.util.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GlobeFragment : Fragment() {

    private val viewModel: GlobeViewModel by viewModels()
    private lateinit var binding: FragmentGlobeBinding
    private val globeGirdAdapter = GlobeAdapter {
        //todo navigation action
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGlobeBinding.inflate(inflater, container, false).apply {
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
        binding.tvIcon.setOnClickListener { findNavController().navigateUp() }
    }

    private fun initAdapter() {
        val largeSpan = 5
        val smallSpan = 3
        val criteriaWidthDp = 500
        binding.rvGlobes.apply {
            adapter = globeGirdAdapter
            binding.rvGlobes.layoutManager = GridLayoutManager(
                requireContext(),
                if (getWidthDp() > criteriaWidthDp) largeSpan else smallSpan
            )
            addItemDecoration(GridSpaceItemDecoration(12.dp, 8.dp, 16.dp, 12.dp, 16.dp, 12.dp))
        }
    }

    private fun getWidthDp(): Float =
        resources.displayMetrics.widthPixels / resources.displayMetrics.density

}