package com.wakeup.presentation.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
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
import com.wakeup.presentation.adapter.DetailPictureAdapter
import com.wakeup.presentation.databinding.FragmentMomentDetailBinding
import com.wakeup.presentation.lib.dialog.NormalDialog
import com.wakeup.presentation.lib.dialog.PictureDialog
import com.wakeup.presentation.model.PictureModel
import com.wakeup.presentation.ui.globe.globedetail.GlobeDetailFragment
import com.wakeup.presentation.util.setToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MomentDetailFragment : Fragment() {
    private lateinit var binding: FragmentMomentDetailBinding
    private val viewModel: MomentDetailViewModel by viewModels()

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
        collectMoment()
        initViewPagerAdapter()
    }

    private fun initToolbar() {
        setToolbar(
            toolbar = binding.tbDetailMoment,
            titleId = R.string.moment,
            onBackClick = { findNavController().navigateUp() }
        )

        binding.tbDetailMoment.tbDefault.apply {
            inflateMenu(R.menu.menu_moment_detail_toolbar)
            setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.item_moment_update -> {
                        val directions =
                            MomentDetailFragmentDirections.actionMomentDetailFragmentToAddMomentNavigation(binding.moment)
                        findNavController().navigate(directions)
                        true
                    }

                    R.id.item_moment_delete -> {
                        showDeleteMomentDialog()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun collectMoment() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.moment.collectLatest { result ->
                    binding.moment = result
                    binding.hasPictures = result.pictures.isNotEmpty()
                }
            }
        }
    }

    private fun initViewPagerAdapter() {
        binding.vp2Images.adapter = DetailPictureAdapter { picture ->
            showDetailPicture(picture)
        }
        binding.tpiIndicator.attachTo(binding.vp2Images, viewLifecycleOwner.lifecycleScope)
    }

    private fun showDetailPicture(picture: PictureModel) {
        val filePath = "${requireContext().filesDir}/" + "images/" + picture.path

        PictureDialog.with(requireContext(), R.layout.dialog_image, R.id.iv_detail_image)
            .setImageSize(width = 1000, height = 1000)
            .setErrorImage(id = R.drawable.ic_no_image)
            .setImagePath(filePath = filePath)
            .show()
    }

    private fun showDeleteMomentDialog() {
        val spannableString =
            SpannableString(getString(R.string.delete_globe_dialog_content_second)).apply {
                setSpan(
                    ForegroundColorSpan(Color.parseColor(GlobeDetailFragment.MAIN_PINK_COLOR_HEX)),
                    GlobeDetailFragment.DELETE_TITLE_SPANNABLE_STRING_START_INDEX,
                    GlobeDetailFragment.DELETE_TITLE_SPANNABLE_STRING_END_INDEX,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        NormalDialog
            .with(requireContext(), R.layout.dialog_delete_moment)
            .setTitle(R.id.tv_delete_dialog_content_second, spannableString)
            .setOnPositive(R.id.tv_delete_dialog_positive, getString(R.string.do_delete)) {
                viewModel.deleteMoment()
                findNavController().popBackStack()
            }
            .setOnNegative(R.id.tv_delete_dialog_negative, getString(R.string.cancel)) {
                Timber.d("CANCEL")
            }
            .show()
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}