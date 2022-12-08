package com.wakeup.presentation.ui.globe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.GlobeDetailAdapter
import com.wakeup.presentation.databinding.FragmentGlobeDetailBinding
import com.wakeup.presentation.extension.dp
import com.wakeup.presentation.extension.showSnackbar
import com.wakeup.presentation.lib.dialog.EditDialog
import com.wakeup.presentation.model.GlobeModel
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.util.setToolbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GlobeDetailFragment : Fragment() {

    private val viewModel: GlobeDetailViewModel by viewModels()
    private lateinit var binding: FragmentGlobeDetailBinding
    private val globeDetailGridAdapter = GlobeDetailAdapter { moment ->
        changeGlobeTitleOfMoment(moment)
    }
    private val args: GlobeDetailFragmentArgs by navArgs()
    private lateinit var argsGlobeName: String
    private var resultTitle: String? = null

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
        argsGlobeName = (args.globe ?: GlobeModel(name = getString(R.string.globe))).name
        initToolbar(resultTitle ?: argsGlobeName)
        initToolbarMenu()
        initAdapter()
    }

    private fun initToolbar(title: String) {
        setToolbar(
            toolbar = binding.tbGlobeDetail,
            titleString = title,
            onBackClick = { findNavController().navigateUp() }
        )
    }

    private fun initToolbarMenu() {
        val globe = args.globe ?: return
        binding.tbGlobeDetail.tbDefault.apply {
            inflateMenu(R.menu.menu_globe_detail_toolbar)
            setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.item_globe_detail_add_moment -> {
                        findNavController().navigate(
                            R.id.action_globe_detail_fragment_to_addMomentInGlobeFragment
                        )
                        true
                    }
                    R.id.item_globe_detail_delete_moment -> {
                        // todo 모먼트 삭제하기
                        true
                    }

                    R.id.item_globe_detail_update_globe -> {
                        showDialog(globe, this)
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

    private fun showDialog(globe: GlobeModel, toolbar: Toolbar) {
        EditDialog
            .with(requireContext(), R.layout.dialog_add_globe, R.id.et_add_globe)
            .setTitle(R.id.tv_add_globe_title,
                getString(R.string.update_globe_name_dialog_title))
            .setOnPositive(R.id.tv_add_globe_add, getString(R.string.update)) { dialog ->
                resultTitle = dialog.getTextInEditText()
                viewModel.updateGlobeTitle(globe, resultTitle ?: argsGlobeName)
                toolbar.showSnackbar(getString(R.string.snack_bar_message_update_globe_name))
                initToolbar(resultTitle ?: argsGlobeName)
            }
            .setOnNegative(R.id.tv_add_globe_cancel, getString(R.string.cancel)) {
                Timber.d("CANCEL")
            }
            .setKeyboardUp(true)
            .show()
    }

    private fun changeGlobeTitleOfMoment(moment: MomentModel): MomentModel {
        return moment.copy(
            globes = moment.globes.map { globe -> globe.copy(name = resultTitle ?: globe.name) }
        )
    }

    private fun initAdapter() {
        val largeSpan = 5
        val smallSpan = 3
        val criteriaWidthDp = 500
        binding.rvGlobeDetail.apply {
            adapter = globeDetailGridAdapter
            layoutManager = GridLayoutManager(
                requireContext(),
                if (getWidthDp() > criteriaWidthDp) largeSpan else smallSpan
            )
            addItemDecoration(GridSpaceItemDecoration(12.dp))
        }
    }

    private fun getWidthDp(): Float =
        resources.displayMetrics.widthPixels / resources.displayMetrics.density

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}