package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemMomentBinding
import com.wakeup.presentation.model.MomentModel
import timber.log.Timber

class MomentPagingAdapter(
    private val isSelectable: Boolean,
    private val itemClickListener: (MomentModel) -> Unit,
) : PagingDataAdapter<MomentModel, MomentPagingAdapter.MomentViewHolder>(MomentDiffCallback) {

    class MomentViewHolder(
        private val binding: ItemMomentBinding,
        itemClickListener: (MomentModel) -> Unit,
        isSelectable: Boolean,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            if (isSelectable) {
                binding.cbSelectMoment.isVisible = true
                binding.viewBlockMoment.isVisible = true
            }
            itemView.setOnClickListener {
                binding.moment?.let { moment ->
                    itemClickListener(moment)
                }
            }
        }

        fun bind(moment: MomentModel) {
            Timber.d("$moment")
            binding.moment = moment
            binding.cbSelectMoment.isChecked = moment.isSelected
            binding.viewBlockMoment.isVisible = moment.isSelected.not()
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                itemClickListener: (moment: MomentModel) -> Unit,
                isSelectable: Boolean,
            ): MomentViewHolder {
                return MomentViewHolder(
                    ItemMomentBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    itemClickListener,
                    isSelectable
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentViewHolder {
        return MomentViewHolder.from(parent, itemClickListener, isSelectable)
    }

    override fun onBindViewHolder(holder: MomentViewHolder, position: Int) {
        val moment = getItem(position)
        if (moment != null) {
            holder.bind(moment)
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

