package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemMomentInGlobeBinding
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.ui.globe.GlobeDetailFragmentDirections

class GlobeDetailAdapter :
    ListAdapter<MomentModel, GlobeDetailAdapter.GlobeMomentViewHolder>(GlobeDetailDiffUtil) {

    class GlobeMomentViewHolder private constructor(
        private val binding: ItemMomentInGlobeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                navigateToMomentDetail()
            }
        }

        private fun navigateToMomentDetail() {
            val action = GlobeDetailFragmentDirections
                .actionGlobeDetailFragmentToMomentDetailFragment(binding.moment ?: return)
            itemView.findNavController().navigate(action)
        }

        fun bind(moment: MomentModel) {
            binding.moment = moment
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): GlobeMomentViewHolder {
                return GlobeMomentViewHolder(
                    ItemMomentInGlobeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobeMomentViewHolder {
        return GlobeMomentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GlobeMomentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object GlobeDetailDiffUtil : DiffUtil.ItemCallback<MomentModel>() {
        override fun areItemsTheSame(oldItem: MomentModel, newItem: MomentModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MomentModel, newItem: MomentModel): Boolean {
            return oldItem == newItem
        }

    }
}