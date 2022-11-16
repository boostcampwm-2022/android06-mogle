package com.wakeup.presentation.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.MomentPagingAdapter
import com.wakeup.presentation.databinding.BottomSheetBinding
import com.wakeup.presentation.databinding.FragmentMapBinding
import timber.log.Timber

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

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
        val items = listOf(getString(R.string.date_sort_asc), getString(R.string.date_sort_desc))
        val menuAdapter = ArrayAdapter(requireContext(), R.layout.item_sort_menu, items)
        (binding.textField.editText as? AutoCompleteTextView)?.setAdapter(menuAdapter)

        binding.sortMenu.setOnItemClickListener { _, _, _, _ ->
            val sortType = binding.sortMenu.text.toString()
            Timber.d(sortType)
            // TODO: 리스트 정렬
        }
    }

    private fun setAdapter(binding: BottomSheetBinding) {
        val adapter = MomentPagingAdapter()
        binding.rvMoments.adapter = adapter
    }
}