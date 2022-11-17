package com.wakeup.presentation.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.MomentPagingAdapter
import com.wakeup.presentation.databinding.BottomSheetBinding
import com.wakeup.presentation.databinding.FragmentMapBinding
import com.wakeup.presentation.model.MomentModel
import kotlinx.coroutines.launch
import timber.log.Timber

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding
    private val momentAdapter = MomentPagingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.bottomSheet) {
            setMenus(this)
            setAdapter(this)
        }
    }

    private fun setMenus(binding: BottomSheetBinding) {
        val items = listOf(getString(R.string.date_sort_desc), getString(R.string.date_sort_asc))
        val menuAdapter = ArrayAdapter(requireContext(), R.layout.item_sort_menu, items)
        (binding.textField.editText as? AutoCompleteTextView)?.setAdapter(menuAdapter)

        binding.sortMenu.setOnItemClickListener { _, _, _, _ ->
            expandBottomSheet(binding.bottomSheet)
            val sortType = binding.sortMenu.text.toString()
            Timber.d(sortType)
            // TODO: 리스트 정렬
        }
    }

    private fun expandBottomSheet(bottomSheet: ConstraintLayout) {
        val behavior = BottomSheetBehavior.from(bottomSheet)
        if (behavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setAdapter(binding: BottomSheetBinding) {
        binding.rvMoments.adapter = momentAdapter
    }
}