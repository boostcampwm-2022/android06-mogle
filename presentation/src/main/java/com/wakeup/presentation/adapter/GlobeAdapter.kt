package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemGlobeBinding
import com.wakeup.presentation.model.GlobeModel
import com.wakeup.presentation.ui.globe.GlobeFragmentDirections

class GlobeAdapter : ListAdapter<GlobeModel, GlobeAdapter.GlobeViewHolder>(GlobeDiffUtil) {

    class GlobeViewHolder private constructor(
        private val binding: ItemGlobeBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { view ->
                binding.globe?.let { globe ->
                    navigateToGlobeDetail(globe, view)
                }
            }
        }

        private fun navigateToGlobeDetail(globe: GlobeModel, view: View) {
            val direction =
                GlobeFragmentDirections.actionGlobeFragmentToGlobeDetailFragment(globe)
            view.findNavController().navigate(direction)
        }

        fun bind(globe: GlobeModel) {
            binding.globe = globe
        }

        companion object {
            fun from(parent: ViewGroup): GlobeViewHolder {
                return GlobeViewHolder(
                    ItemGlobeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobeViewHolder {
        return GlobeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GlobeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object GlobeDiffUtil : DiffUtil.ItemCallback<GlobeModel>() {
        override fun areItemsTheSame(oldItem: GlobeModel, newItem: GlobeModel): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: GlobeModel, newItem: GlobeModel): Boolean {
            return oldItem == newItem
        }
    }
}