package com.wakeup.presentation.ui.globe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.GlobeAdapter
import com.wakeup.presentation.databinding.FragmentGlobeBinding
import com.wakeup.presentation.extension.dp
import com.wakeup.presentation.lib.MogleDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GlobeFragment : Fragment() {

    private val viewModel: GlobeViewModel by viewModels()
    private lateinit var binding: FragmentGlobeBinding
    private val globeGirdAdapter = GlobeAdapter()

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
    }

    private fun initToolbar() {
        binding.ivBackButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun initAdapter() {
        val largeSpan = 5
        val smallSpan = 3
        val criteriaWidthDp = 500
        binding.rvGlobes.apply {
            adapter = globeGirdAdapter
            layoutManager = GridLayoutManager(
                requireContext(),
                if (getWidthDp() > criteriaWidthDp) largeSpan else smallSpan
            )
            addItemDecoration(GridSpaceItemDecoration(12.dp, 8.dp, 16.dp, 12.dp, 16.dp, 12.dp))
        }
    }

    private fun getWidthDp(): Float =
        resources.displayMetrics.widthPixels / resources.displayMetrics.density

    private fun initClickListener() {
        binding.ivAddGlobeButton.setOnClickListener {
            val mogleDialog = MogleDialog.with(requireContext(), R.layout.dialog_add_globe, R.id.et_add_globe)
            mogleDialog.setOnPositive(R.id.tv_add_globe_add) {
                    viewModel.createGlobe(mogleDialog.gettextInEditText())
                    Timber.d("OK")
                    showSnackBar(getString(R.string.snack_bar_message_add_globe))
                }
                .setOnNegative(R.id.tv_add_globe_cancel) { Timber.d("CANCEL") }
                .setFocusEditTextAndKeyboardUp()
                .show()
        }
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
        snackBar.show()
    }
}