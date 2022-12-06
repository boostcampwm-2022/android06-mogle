package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.R
import com.wakeup.presentation.databinding.ItemMomentBinding
import com.wakeup.presentation.model.MomentModel
import com.wakeup.presentation.ui.home.HomeFragmentDirections
import timber.log.Timber

class MomentPagingAdapter :
    PagingDataAdapter<MomentModel, MomentPagingAdapter.MomentViewHolder>(MomentDiffCallback) {

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
        private val binding: ItemMomentBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                navigateToMoment()
            }
        }

        private fun navigateToMoment() {
            val action = HomeFragmentDirections
                .actionMapFragmentToMomentDetailFragment(binding.moment ?: return)
            itemView.findNavController().navigate(action)
        }

        fun bind(moment: MomentModel) {
            Timber.d("$moment")
            binding.moment = moment
            binding.executePendingBindings()
        }
    }

    companion object MomentDiffCallback : DiffUtil.ItemCallback<MomentModel>() {
        override fun areItemsTheSame(oldItem: MomentModel, newItem: MomentModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MomentModel, newItem: MomentModel): Boolean {
            return oldItem == newItem
        }
    }
}

