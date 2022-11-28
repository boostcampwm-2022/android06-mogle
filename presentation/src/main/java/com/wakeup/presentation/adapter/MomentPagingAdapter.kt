package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.R
import com.wakeup.presentation.databinding.ItemMomentBinding
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.ui.map.MapFragmentDirections

class MomentPagingAdapter :
    PagingDataAdapter<MomentModel, MomentPagingAdapter.MomentViewHolder>(MomentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentViewHolder {
        return MomentViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_moment,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MomentViewHolder, position: Int) {
        val moment = getItem(position)
        if (moment != null) {
            holder.bind(moment)
        }
    }

    class MomentViewHolder(
        private val binding: ItemMomentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener { view ->
                binding.moment?.let { moment ->
                    navigateToMoment(moment, view)
                }
            }
        }

        private fun navigateToMoment(moment: MomentModel, view: View) {
            val direction =
                MapFragmentDirections.actionMapFragmentToMomentDetailFragment(moment)
            view.findNavController().navigate(direction)
        }

        fun bind(moment: MomentModel) {
            with(binding) {
                this.moment = moment
                executePendingBindings()
            }
        }
    }
}

private class MomentDiffCallback : DiffUtil.ItemCallback<MomentModel>() {

    override fun areItemsTheSame(oldItem: MomentModel, newItem: MomentModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MomentModel, newItem: MomentModel): Boolean {
        return oldItem == newItem
    }
}