package com.wakeup.presentation.ui.map

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.MomentPagingAdapter
import com.wakeup.presentation.databinding.MapBottomSheetBinding

class MapBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: MapBottomSheetBinding

    private fun setAdapter() {
        val adapter = MomentPagingAdapter()
        binding.rvMoments.adapter = adapter
    }

    private fun setMenus() {
        val items = listOf(getString(R.string.date_sort_asc), getString(R.string.date_sort_desc))
        val menuAdapter = ArrayAdapter(requireContext(), R.layout.item_sort_menu, items)
        (binding.textField.editText as? AutoCompleteTextView)?.setAdapter(menuAdapter)

        binding.sortMenu.setOnItemClickListener { _, _, _, _ ->
            val sortType = binding.sortMenu.text.toString()
            // TODO: 리스트 정렬
        }
    }
}