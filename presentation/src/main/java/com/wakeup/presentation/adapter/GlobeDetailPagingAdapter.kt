package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemMomentInGlobeBinding
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.ui.globe.globedetail.GlobeDetailFragmentDirections

class GlobeDetailPagingAdapter(
    private val changeGlobeTitleOfMoment: (MomentModel) -> MomentModel,
) : PagingDataAdapter<MomentModel, GlobeDetailPagingAdapter.GlobeMomentViewHolder>(GlobeDetailDiffUtil) {

    class GlobeMomentViewHolder private constructor(
        private val binding: ItemMomentInGlobeBinding,
        private val changeGlobeTitleOfMoment: (MomentModel) -> MomentModel,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                navigateToMomentDetail()
            }
        }

        private fun navigateToMomentDetail() {
            val argMoment = changeGlobeTitleOfMoment(binding.moment ?: return)
            val action = GlobeDetailFragmentDirections
                .actionGlobeDetailFragmentToMomentDetailFragment(argMoment.id)
            itemView.findNavController().navigate(action)
        }

        fun bind(moment: MomentModel) {
            binding.moment = moment
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                changeGlobeTitleOfMoment: (MomentModel) -> MomentModel,
            ): GlobeMomentViewHolder {
                return GlobeMomentViewHolder(
                    ItemMomentInGlobeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    changeGlobeTitleOfMoment
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobeMomentViewHolder {
        return GlobeMomentViewHolder.from(parent, changeGlobeTitleOfMoment)
    }

    override fun onBindViewHolder(holder: GlobeMomentViewHolder, position: Int) {
        val moment = getItem(position)
        if (moment != null) {
            holder.bind(moment)
        }
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