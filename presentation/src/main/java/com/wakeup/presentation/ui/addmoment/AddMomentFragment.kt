package com.wakeup.presentation.ui.addmoment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.PictureAdapter
import com.wakeup.presentation.databinding.FragmentAddMomentBinding
import com.wakeup.presentation.model.PictureModel
import com.wakeup.presentation.util.BitmapUtil.fixRotation
import com.wakeup.presentation.util.DateUtil
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddMomentFragment : Fragment() {

    private val viewModel: AddMomentViewModel by viewModels()
    private val adapter = PictureAdapter { viewModel.removePicture(it) }
    private lateinit var binding: FragmentAddMomentBinding
    private val getPicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val uri = result.data?.data ?: return@registerForActivityResult
            val contentResolver = requireContext().contentResolver
            kotlin.runCatching {
                listOf(contentResolver.openInputStream(uri), contentResolver.openInputStream(uri))
            }.onSuccess {
                val bitmap = BitmapFactory.decodeStream(it.first())
                viewModel.addPicture(PictureModel(bitmap.fixRotation(it.last())))
                it.forEach { stream -> stream?.close() }
            }.onFailure {
                Timber.e(it)
            }
        }

    private val datePicker = MaterialDatePicker.Builder.datePicker().build().apply {
        addOnPositiveButtonClickListener { date ->
            viewModel.setSelectedDate(date)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddMomentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            rvPicture.adapter = adapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.cvAddPicture.setOnClickListener {
            getPicture.launch(viewModel.getPictureIntent())
        }

        binding.actGlobe.setOnItemClickListener { _, _, position, _ ->
            viewModel.setSelectedGlobe(position)
        }

        binding.tvDateValue.setOnClickListener {
            datePicker.show(childFragmentManager, "datePicker")
        }

        binding.tvPlaceValue.setOnClickListener {
            findNavController().navigate(R.id.action_addMoment_to_placeSearch)
        }

        binding.tvSave.setOnClickListener {
            viewModel.saveMoment()
            findNavController().popBackStack()
        }

        super.onViewCreated(view, savedInstanceState)
    }
}