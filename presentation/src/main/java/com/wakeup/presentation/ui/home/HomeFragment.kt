package com.wakeup.presentation.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wakeup.domain.model.SortType
import com.wakeup.presentation.R
import com.wakeup.presentation.databinding.FragmentHomeBinding
import com.wakeup.presentation.extension.getNavigationResultFromTop
import com.wakeup.presentation.extension.hideKeyboard
import com.wakeup.presentation.ui.MainActivity
import com.wakeup.presentation.ui.home.map.MapFragment
import com.wakeup.presentation.ui.home.sheet.BottomSheetFragment
import com.wakeup.presentation.util.UPDATE_MOMENTS_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMap()
        initBottomSheet()
        initializeBroadcastReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSearchBarListener()
    }

    private fun initMap() {
        if (childFragmentManager.findFragmentById(R.id.map) == null) {
            val mapFragment = MapFragment()
            childFragmentManager.beginTransaction().add(R.id.map, mapFragment).commit()
        }
    }

    private fun initBottomSheet() {
        if (childFragmentManager.findFragmentById(R.id.bottom_sheet) == null) {
            val bottomSheetFragment = BottomSheetFragment()
            childFragmentManager.beginTransaction().add(R.id.bottom_sheet, bottomSheetFragment).commit()
        }
    }

    private fun setSearchBarListener() {
        binding.ivMenu.setOnClickListener {
            (activity as MainActivity).openNavDrawer()
        }

        binding.etSearch.setOnEditorActionListener { textView, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setScrollToTop(true)
                viewModel.setSearchQuery(textView.text.toString())
                viewModel.fetchMoments()

                hideKeyboard()
            }
            false // true: 계속 search 가능
        }
    }

    private fun initializeBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                viewModel.setScrollToTop(true)
                viewModel.sortType.value = SortType.MOST_RECENT
                viewModel.fetchMoments()
            }
        }

        requireActivity().registerReceiver(
            broadcastReceiver,
            IntentFilter(UPDATE_MOMENTS_KEY)
        )
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}