package com.wakeup.presentation.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wakeup.presentation.databinding.MapBottomSheetBinding

class MapBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: MapBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }
}