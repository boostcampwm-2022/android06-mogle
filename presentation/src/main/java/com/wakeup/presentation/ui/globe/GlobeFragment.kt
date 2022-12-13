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
import com.wakeup.presentation.adapter.GlobeAdapter
import com.wakeup.presentation.databinding.FragmentGlobeBinding
import com.wakeup.presentation.extension.dp
import com.wakeup.presentation.extension.showSnackBar
import com.wakeup.presentation.lib.dialog.EditDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GlobeFragment : Fragment() {

    private val viewModel: GlobeViewModel by viewModels()
    private lateinit var binding: FragmentGlobeBinding
    private val globeGridAdapter = GlobeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        initClickListener()
        initGlobes()
    }

    private fun initToolbar() {
        binding.ivBackButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun initAdapter() {
        val largeSpan = LARGE_SPAN
        val smallSpan = SMALL_SPAN
        val criteriaWidthDp = CRITERIA_WIDTH_DP
        binding.rvGlobes.apply {
            adapter = globeGridAdapter
            layoutManager = GridLayoutManager(
                requireContext(),
                if (getWidthDp() > criteriaWidthDp) largeSpan else smallSpan
            )
            addItemDecoration(GridSpaceItemDecoration(GRID_SPACE_PX.dp))
        }
    }

    private fun getWidthDp(): Float =
        resources.displayMetrics.widthPixels / resources.displayMetrics.density

    private fun initClickListener() {
        fun onPositive(dialog: EditDialog) {
            if (viewModel.isExistGlobe(dialog.getTextInEditText())) {
                binding.tbGlobe.showSnackBar(
                    getString(R.string.snack_bar_error_message_add_globe),
                    R.id.tb_globe
                )
            } else {
                viewModel.createGlobe(dialog.getTextInEditText())
                binding.tbGlobe.showSnackBar(
                    getString(R.string.snack_bar_message_add_globe),
                    R.id.tb_globe
                )
            }
        }

        fun onNegative() {
            Timber.d("CANCEL")
        }

        binding.ivAddGlobeButton.setOnClickListener {
            EditDialog
                .with(requireContext(), R.layout.dialog_add_globe, R.id.et_add_globe)
                .setTitle(R.id.tv_add_globe_title, getString(R.string.add_globe_dialog_title))
                .setOnPositive(R.id.tv_add_globe_add, getString(R.string.add)) { dialog ->
                    onPositive(dialog)
                }
                .setOnNegative(R.id.tv_add_globe_cancel) {
                    onNegative()
                }
                .setKeyboardUp(true)
                .show()
        }
    }

    private fun initGlobes() {
        viewModel.fetchGlobes()
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }

    companion object {
        const val CRITERIA_WIDTH_DP = 500
        const val LARGE_SPAN = 5
        const val SMALL_SPAN = 3
        const val GRID_SPACE_PX = 12
    }
}