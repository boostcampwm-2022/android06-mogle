package com.wakeup.presentation.ui.globe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wakeup.presentation.databinding.FragmentGlobeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GlobeFragment : Fragment() {

    private val viewModel: GlobeViewModel by viewModels()
    private lateinit var binding: FragmentGlobeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGlobeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
    }

    private fun initToolbar() {
        binding.tvIcon.setOnClickListener { findNavController().navigateUp() }
    }
}