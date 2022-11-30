package com.wakeup.presentation.ui.home

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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMap()
        initBottomSheet()
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
        updateMoments()
    }

    private fun initMap() {
        val mapFragment = MapFragment()
        childFragmentManager.beginTransaction().add(R.id.map, mapFragment).commit()
    }

    private fun initBottomSheet() {
        val bottomSheetFragment = BottomSheetFragment()
        childFragmentManager.beginTransaction().add(R.id.bottom_sheet, bottomSheetFragment).commit()
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

    private fun updateMoments() {
        findNavController().getNavigationResultFromTop<Boolean>("isUpdated")
            ?.observe(viewLifecycleOwner) { isUpdated ->
                viewModel.setScrollToTop(isUpdated)
                if (isUpdated) {
                    viewModel.sortType.value = SortType.MOST_RECENT
                    viewModel.fetchMoments()
                }
            }
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}