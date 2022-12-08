package com.wakeup.presentation.ui.addmoment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.PlaceAdapter
import com.wakeup.presentation.databinding.FragmentPlaceSearchBinding
import com.wakeup.presentation.util.setToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceSearchFragment : Fragment() {

    private val viewModel: PlaceSearchViewModel by viewModels()
    private lateinit var binding: FragmentPlaceSearchBinding
    private val adapter = PlaceAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceSearchBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initToolbar()

    }

    private fun initAdapter() {
        binding.rvSearchResult.adapter = adapter
    }

    private fun initToolbar() {
        setToolbar(
            toolbar = binding.tbPlaceSearch,
            titleId = R.string.place_search,
            onBackClick = { findNavController().navigateUp() }
        )
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}