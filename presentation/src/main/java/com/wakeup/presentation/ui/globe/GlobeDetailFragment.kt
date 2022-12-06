package com.wakeup.presentation.ui.globe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.GlobeDetailAdapter
import com.wakeup.presentation.databinding.FragmentGlobeDetailBinding
import com.wakeup.presentation.extension.dp
import com.wakeup.presentation.util.setToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GlobeDetailFragment : Fragment() {

    private val viewModel: GlobeDetailViewModel by viewModels()
    private lateinit var binding: FragmentGlobeDetailBinding
    private val globeDetailGirdAdapter = GlobeDetailAdapter()
    private val args: GlobeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGlobeDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initAdapter()
        initMoment()
    }

    private fun initToolbar() {
        setToolbar(
            toolbar = binding.tbGlobeDetail,
            titleString = (args.globe ?: return).name,
            onBackClick = { findNavController().navigateUp() }
        )
        binding.tbGlobeDetail.tbDefault.apply {
            inflateMenu(R.menu.menu_globe_detail_toolbar)
            setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.item_globe_detail_add_moment -> {
                        val action =
                            GlobeDetailFragmentDirections.actionGlobeDetailFragmentToAddMomentInGlobeFragment()
                        findNavController().navigate(action)
                        true
                    }
                    R.id.item_globe_detail_delete_moment -> {
                        // todo 모먼트 삭제하기
                        true
                    }

                    R.id.item_globe_detail_update_globe -> {
                        // todo 글로브 이름 변경하기
                        true
                    }
                    R.id.item_globe_detail_delete_globe -> {
                        // todo 글로브 삭제하기
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun initAdapter() {
        val largeSpan = 5
        val smallSpan = 3
        val criteriaWidthDp = 500
        binding.rvGlobeDetail.apply {
            adapter = globeDetailGirdAdapter
            layoutManager = GridLayoutManager(
                requireContext(),
                if (getWidthDp() > criteriaWidthDp) largeSpan else smallSpan
            )
            addItemDecoration(GridSpaceItemDecoration(12.dp))
        }
    }

    private fun initMoment() {
        viewModel.fetchMomentsByGlobe(args.globe?.id ?: -1L)
    }

    private fun getWidthDp(): Float =
        resources.displayMetrics.widthPixels / resources.displayMetrics.density
}