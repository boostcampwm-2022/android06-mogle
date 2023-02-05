package com.wakeup.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wakeup.presentation.R
import com.wakeup.presentation.databinding.FragmentPolicyBinding
import com.wakeup.presentation.util.setToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyFragment : Fragment() {
    private lateinit var binding: FragmentPolicyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPolicyBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setText()
    }

    private fun initToolbar() {
        setToolbar(
            toolbar = binding.tbPolicy,
            titleId = R.string.tv_personal_information_processing_policy,
            onBackClick = { findNavController().navigateUp() }
        )
    }

    fun setText() {
        binding.tvPolicy.text =
            getString(R.string.tv_personal_information_processing_policy_content)
    }
}