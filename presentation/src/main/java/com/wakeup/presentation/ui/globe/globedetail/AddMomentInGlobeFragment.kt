package com.wakeup.presentation.ui.globe.globedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.MomentPagingAdapter
import com.wakeup.presentation.adapter.SaveReadyMomentAdapter
import com.wakeup.presentation.databinding.FragmentAddMomentInGlobeBinding
import com.wakeup.presentation.extension.dp
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.ui.globe.GridSpaceItemDecoration
import com.wakeup.presentation.util.setToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddMomentInGlobeFragment : Fragment() {

    private val viewModel: AddMomentInGlobeViewModel by viewModels()
    private lateinit var binding: FragmentAddMomentInGlobeBinding
    private val momentPagingAdapter = MomentPagingAdapter(isSelectable = true) { moment, position ->
        selectMoment(moment, position)
    }
    private val saveReadyMomentAdapter = SaveReadyMomentAdapter()

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
        collectData()
    }

    private fun initToolbar() {
        setToolbar(
            toolbar = binding.tbAddMomentInGlobe,
            titleId = R.string.add_moment_in_globe,
            onBackClick = { findNavController().navigateUp() }
        )
    }

    private fun initAdapter() {
        binding.rvAddMomentInGlobe.apply {
            adapter = momentPagingAdapter.apply {
                addLoadStateListener {
                    viewModel.setMomentExist(momentPagingAdapter.itemCount != 0)
                }
            }
            addItemDecoration(GridSpaceItemDecoration(12.dp))
        }
        binding.rvSaveMomentInGlobe.adapter = saveReadyMomentAdapter
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.moments.collectLatest {
                    momentPagingAdapter.submitData(it)
                }
            }
        }
    }

    private fun selectMoment(moment: MomentModel, position: Int) {
        moment.isSelected = moment.isSelected.not()
        viewModel.setSaveReadyMoments(moment)
        momentPagingAdapter.notifyItemChanged(position)
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}