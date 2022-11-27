package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemGlobeBinding
import com.wakeup.presentation.model.GlobeModel

class GlobeAdapter(
    private val onClickGlobeDetail: () -> Unit,
) : ListAdapter<GlobeModel, GlobeAdapter.GlobeViewHolder>(diffCallback) {

    class GlobeViewHolder private constructor(
        private val binding: ItemGlobeBinding,
        private val onClickGlobeDetail: () -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.layoutGlobeItem.setOnClickListener {
                onClickGlobeDetail()
            }
        }

        fun bind(globe: GlobeModel) {
            binding.tvGlobeItemTitle.text = globe.name
        }

        companion object {
            fun from(parent: android.view.ViewGroup, onClickGlobeDetail: () -> Unit) = GlobeViewHolder(
                ItemGlobeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onClickGlobeDetail
            )
        }
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): GlobeViewHolder {
        return GlobeViewHolder.from(parent, onClickGlobeDetail)
    }

    override fun onBindViewHolder(holder: GlobeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallback =
            object : androidx.recyclerview.widget.DiffUtil.ItemCallback<GlobeModel>() {
                override fun areItemsTheSame(oldItem: GlobeModel, newItem: GlobeModel): Boolean =
                    oldItem.name == newItem.name

                override fun areContentsTheSame(oldItem: GlobeModel, newItem: GlobeModel): Boolean =
                    oldItem == newItem

            }
    }
}