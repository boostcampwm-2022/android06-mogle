package com.wakeup.presentation.ui.globe.globedetail

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.GlobeDetailPagingAdapter
import com.wakeup.presentation.databinding.FragmentGlobeDetailBinding
import com.wakeup.presentation.extension.showSnackBar
import com.wakeup.presentation.lib.dialog.EditDialog
import com.wakeup.presentation.lib.dialog.NormalDialog
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.util.setToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class GlobeDetailFragment : Fragment() {

    private val viewModel: GlobeDetailViewModel by viewModels()
    private lateinit var binding: FragmentGlobeDetailBinding
    private val globeDetailGridAdapter = GlobeDetailPagingAdapter { moment ->
        changeGlobeTitleOfMoment(moment)
    }
    private val args: GlobeDetailFragmentArgs by navArgs()
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
        initToolbar(resultTitle ?: args.globe.name)
        initToolbarMenu()
        initAdapter()
        collectData()
    }

    private fun initToolbar(title: String) {
        setToolbar(
            toolbar = binding.tbGlobeDetail,
            titleString = title,
            onBackClick = { findNavController().navigateUp() }
        )
    }

    private fun initToolbarMenu() {
        binding.tbGlobeDetail.tbDefault.apply {
            inflateMenu(R.menu.menu_globe_detail_toolbar)
            setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.item_globe_detail_add_moment -> {
                        if (args.globe.id == DEFAULT_GLOBE_ID) {
                            showSnackBar(
                                getString(R.string.add_moment_default_error_in_globe_msg),
                                R.id.tb_globe_detail
                            )
                        } else {
                            val action = GlobeDetailFragmentDirections
                                .actionGlobeDetailFragmentToAddMomentInGlobeFragment(args.globe)
                            findNavController().navigate(action)
                        }
                        true
                    }
                    R.id.item_globe_detail_delete_moment -> {
                        when {
                            args.globe.id == DEFAULT_GLOBE_ID -> {
                                showSnackBar(
                                    getString(R.string.delete_moment_default_error_in_globe_msg),
                                    R.id.tb_globe_detail
                                )
                            }
                            viewModel.isExistMoment.value -> {
                                val action = GlobeDetailFragmentDirections
                                    .actionGlobeDetailFragmentToDeleteMomentInGlobeFragment(args.globe)
                                findNavController().navigate(action)
                            }
                            else -> {
                                showSnackBar(
                                    getString(R.string.delete_moment_error_in_globe_msg),
                                    R.id.tb_globe_detail
                                )
                            }
                        }
                        true
                    }
                    R.id.item_globe_detail_update_globe -> {
                        showUpdateGlobeNameDialog(this)
                        true
                    }
                    R.id.item_globe_detail_delete_globe -> {
                        showDeleteGlobeDialog(this)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun showDeleteGlobeDialog(toolbar: Toolbar) {
        fun onPositive() {
            if (args.globe.id == DEFAULT_GLOBE_ID) {
                toolbar.showSnackBar(
                    getString(R.string.delete_default_globe_error_msg),
                    R.id.tb_globe_detail
                )
            } else {
                viewModel.deleteGlobe()
                findNavController().navigateUp()
            }
        }

        fun onNegative() {
            Timber.d("CANCEL")
        }

        val spannableString =
            SpannableString(getString(R.string.delete_globe_dialog_content_second)).apply {
                setSpan(
                    ForegroundColorSpan(Color.parseColor(MAIN_PINK_COLOR_HEX)),
                    DELETE_TITLE_SPANNABLE_STRING_START_INDEX,
                    DELETE_TITLE_SPANNABLE_STRING_END_INDEX,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        NormalDialog
            .with(requireContext(), R.layout.dialog_delete_globe)
            .setTitle(R.id.tv_delete_dialog_content_second, spannableString)
            .setOnPositive(R.id.tv_delete_dialog_positive, getString(R.string.do_delete)) {
                onPositive()
            }
            .setOnNegative(R.id.tv_delete_dialog_negative) {
                onNegative()
            }
            .show()
    }

    private fun showUpdateGlobeNameDialog(toolbar: Toolbar) {
        fun onPositive(dialog: EditDialog) {
            resultTitle = dialog.getTextInEditText()
            val updateReadyName = resultTitle ?: args.globe.name
            if (viewModel.isExistGlobe(updateReadyName)) {
                toolbar.showSnackBar(
                    getString(R.string.snack_bar_message_error_update_globe_name),
                    R.id.tb_globe_detail
                )
            } else {
                viewModel.updateGlobeTitle(updateReadyName)
                toolbar.showSnackBar(
                    getString(R.string.snack_bar_message_update_globe_name),
                    R.id.tb_globe_detail
                )
            }
            initToolbar(updateReadyName)
        }

        fun onNegative() {
            Timber.d("CANCEL")
        }

        EditDialog
            .with(requireContext(), R.layout.dialog_add_globe, R.id.et_add_globe)
            .setTitle(R.id.tv_add_globe_title, getString(R.string.update_globe_name_dialog_title))
            .setOnPositive(R.id.tv_add_globe_add, getString(R.string.update)) { dialog ->
                onPositive(dialog)
            }
            .setOnNegative(R.id.tv_add_globe_cancel) {
                onNegative()
            }
            .setKeyboardUp(true)
            .show()
    }

    private fun changeGlobeTitleOfMoment(moment: MomentModel): MomentModel {
        return moment.copy(
            globes = moment.globes.map { globe -> globe.copy(name = resultTitle ?: globe.name) }
        )
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.moments.collectLatest {
                    globeDetailGridAdapter.submitData(it)
                }
            }
        }
    }

    private fun initAdapter() {
        val largeSpan = LARGE_SPAN
        val smallSpan = SMALL_SPAN
        val criteriaWidthDp = CRITERIA_WIDTH_DP
        binding.rvGlobeDetail.apply {
            adapter = globeDetailGridAdapter.apply {
                addLoadStateListener {
                    viewModel.setMomentExist(globeDetailGridAdapter.itemCount != 0)
                }
            }
            layoutManager = GridLayoutManager(
                requireContext(),
                if (getWidthDp() > criteriaWidthDp) largeSpan else smallSpan
            )
        }
    }

    private fun getWidthDp(): Float =
        resources.displayMetrics.widthPixels / resources.displayMetrics.density

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }

    companion object {
        const val MAIN_PINK_COLOR_HEX = "#EA698F"
        const val DELETE_TITLE_SPANNABLE_STRING_START_INDEX = 0
        const val DELETE_TITLE_SPANNABLE_STRING_END_INDEX = 2

        const val DEFAULT_GLOBE_ID = 1L

        const val CRITERIA_WIDTH_DP = 500
        const val LARGE_SPAN = 5
        const val SMALL_SPAN = 3
    }
}