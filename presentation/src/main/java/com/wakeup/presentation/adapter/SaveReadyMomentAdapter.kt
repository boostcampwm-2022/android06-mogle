package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemMomentInGlobeBinding
import com.wakeup.presentation.model.MomentModel

class SaveReadyMomentAdapter
    : ListAdapter<MomentModel, SaveReadyMomentAdapter.ReadyMomentViewHolder>(ReadyMomentDiffUtil) {

    class ReadyMomentViewHolder private constructor(
        private val binding: ItemMomentInGlobeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                // todo delete Selected MomentModel
            }
        }

        fun bind(moment: MomentModel) {
            binding.moment = moment
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ReadyMomentViewHolder {
                return ReadyMomentViewHolder(
                    ItemMomentInGlobeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReadyMomentViewHolder {
        return ReadyMomentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ReadyMomentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object ReadyMomentDiffUtil : DiffUtil.ItemCallback<MomentModel>() {
        override fun areItemsTheSame(oldItem: MomentModel, newItem: MomentModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MomentModel, newItem: MomentModel): Boolean {
            return oldItem == newItem
        }

    }
}
