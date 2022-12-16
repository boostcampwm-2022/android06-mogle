package com.wakeup.presentation.ui.addmoment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.PictureAdapter
import com.wakeup.presentation.databinding.FragmentAddMomentBinding
import com.wakeup.presentation.extension.setNavigationResultToBackStack
import com.wakeup.presentation.lib.dialog.NormalDialog
import com.wakeup.presentation.model.PictureModel
import com.wakeup.presentation.ui.UiState
import com.wakeup.presentation.util.MOVE_CAMERA_KEY
import com.wakeup.presentation.util.setToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AddMomentFragment : Fragment() {

    private val viewModel: AddMomentViewModel by navGraphViewModels(R.id.add_moment_navigation) {
        defaultViewModelProviderFactory
    }
    private val args: AddMomentFragmentArgs by navArgs()
    private val adapter = PictureAdapter { viewModel.removePicture(it) }
    private lateinit var binding: FragmentAddMomentBinding
    private val getPicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val uri = result.data?.data ?: return@registerForActivityResult
            viewModel.addPicture(PictureModel(path = uri.toString()))
        }

    private val constraintsBuilder =
        CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now())

    private val datePicker = MaterialDatePicker
        .Builder
        .datePicker()
        .setCalendarConstraints(constraintsBuilder.build())
        .build()
        .apply {
            addOnPositiveButtonClickListener { date ->
                viewModel.setSelectedDate(date)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddMomentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initToolbar()
        initAddPicture()
        initGlobe()
        initDate()
        initPlace()
        initSave()

    }

    private fun initAdapter() {
        binding.rvPicture.adapter = adapter
    }

    private fun initToolbar() {
        setToolbar(
            toolbar = binding.tbAddMoment,
            titleId = R.string.add_moment,
            onBackClick = { findNavController().navigateUp() }
        )
    }

    private fun initAddPicture() {
        binding.cvAddPicture.setOnClickListener {
            getPicture.launch(viewModel.getPictureIntent())
        }
    }

    private fun initGlobe() {
        binding.actGlobe.setOnItemClickListener { _, _, position, _ ->
            viewModel.setSelectedGlobe(position)
        }
    }

    private fun initDate() {
        binding.tvDateValue.setOnClickListener {
            if (datePicker.isAdded) return@setOnClickListener
            datePicker.show(childFragmentManager, "datePicker")
        }
    }

    private fun initPlace() {
        binding.tvPlaceValue.setOnClickListener {
            findNavController().navigate(R.id.action_addMoment_to_placeSearch)
        }
        args.place?.let {
            viewModel.setPlace(it)
        }
    }

    private fun initSave() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSaveButtonClicked.collect { isClicked ->
                    if (isClicked.not()) return@collect

                    if (viewModel.argMoment == null) {
                        viewModel.saveMoment()
                    } else {
                        viewModel.updateMoment()
                    }
                }
            }
        }

        val dialog = NormalDialog.with(requireContext(), R.layout.dialog_loading)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state is UiState.Loading) {
                        dialog.show()
                    } else {
                        dialog.dismiss()
                    }

                    if (state is UiState.Success) {
                        val msg = if (viewModel.argMoment == null) {
                            getString(R.string.write_moment)
                        } else {
                            getString(R.string.update_moment)
                        }

                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        findNavController().setNavigationResultToBackStack(MOVE_CAMERA_KEY, viewModel.place.value.location)
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}