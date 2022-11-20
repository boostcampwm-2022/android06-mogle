package com.wakeup.presentation.ui.addmoment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wakeup.presentation.adapter.PlaceAdapter
import com.wakeup.presentation.databinding.FragmentPlaceSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PlaceSearchFragment : Fragment() {

    private val viewModel: PlaceSearchViewModel by viewModels()
    private lateinit var binding: FragmentPlaceSearchBinding
    private val adapter = PlaceAdapter { Timber.d("$it") }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceSearchBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            rvSearchResult.adapter = adapter
        }

        return binding.root
    }
}