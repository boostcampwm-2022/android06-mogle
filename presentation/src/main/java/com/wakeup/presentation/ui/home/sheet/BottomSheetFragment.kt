package com.wakeup.presentation.ui.home.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.wakeup.domain.model.SortType
import com.wakeup.presentation.R
import com.wakeup.presentation.adapter.MomentPagingAdapter
import com.wakeup.presentation.databinding.FragmentBottomSheetBinding
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.ui.home.HomeFragmentDirections
import com.wakeup.presentation.ui.home.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetBinding
    private val viewModel: HomeViewModel by viewModels({ requireParentFragment() })
    private val momentAdapter = MomentPagingAdapter(isSelectable = false) { moment, _ ->
        navigateToMoment(moment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMenus()
        setBottomSheetState()
        setAdapter()
        setAdapterListener()
        setBottomSheetCallback()
        setBackPressCallback()

        collectMoments()
    }

    private fun setMenus() {
        val items = SortType.values().map { it.str }
        val menuAdapter = ArrayAdapter(requireContext(), R.layout.item_menu, items)
        binding.textField.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (viewModel.bottomSheetState.value == BottomSheetBehavior.STATE_EXPANDED) {
                    changeVisibleMenu(true)
                }

                binding.sortMenu.setText(viewModel.sortType.value.str)
                (binding.textField.editText as? MaterialAutoCompleteTextView)?.setAdapter(
                    menuAdapter
                )
                binding.textField.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        binding.sortMenu.setOnItemClickListener { _, _, position, _ ->
            viewModel.sortType.value = when (position) {
                0 -> SortType.MOST_RECENT
                1 -> SortType.OLDEST
                else -> {
                    viewModel.fetchLocationState.value = true
                    SortType.NEAREST
                }
            }
            viewModel.fetchMoments()
            collectMoments()
        }
    }

    private fun setBottomSheetState() {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet)
        behavior.state = viewModel.bottomSheetState.value
    }

    private fun setAdapter() {
        binding.rvMoments.adapter = momentAdapter
    }

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            if (viewModel.scrollToTop.value) {
                binding.rvMoments.scrollToPosition(0)
            }
            viewModel.setScrollToTop(false)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            binding.rvMoments.scrollToPosition(0)
        }
    }

    private fun setAdapterListener() {
        momentAdapter.registerAdapterDataObserver(adapterDataObserver)

        momentAdapter.addLoadStateListener {
            binding.hasMoments = momentAdapter.itemCount > 0
        }
    }

    fun collectMoments() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.moments.collectLatest {
                    momentAdapter.submitData(it)
                }
            }
        }
    }

    private fun setBottomSheetCallback() {
        val behavior = BottomSheetBehavior.from(binding.bottomSheet)
        if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            binding.textField.visibility = View.INVISIBLE
        }

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (viewModel.bottomSheetState.value == newState) return

                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        changeVisibleMenu(true)
                        viewModel.bottomSheetState.value = BottomSheetBehavior.STATE_EXPANDED
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        changeVisibleMenu(false)
                        viewModel.bottomSheetState.value = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        })
    }

    private fun changeVisibleMenu(isVisible: Boolean) = with(binding.textField) {
        if (isVisible) {
            visibility = View.VISIBLE
            animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        } else {
            visibility = View.INVISIBLE
            animation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        }
        isEnabled = isVisible
    }

    private fun navigateToMoment(moment: MomentModel) {
        val action = HomeFragmentDirections
            .actionMapFragmentToMomentDetailFragment(moment.id)
        findNavController().navigate(action)
    }

    private fun setBackPressCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val behavior = BottomSheetBehavior.from(binding.bottomSheet)
                if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    this.isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onDestroyView() {
        momentAdapter.unregisterAdapterDataObserver(adapterDataObserver)
        binding.unbind()
        super.onDestroyView()
    }
}