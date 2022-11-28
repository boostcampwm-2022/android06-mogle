package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemGlobeBinding
import com.wakeup.presentation.model.GlobeModel

class GlobeAdapter(
    private val onClickGlobeDetail: () -> Unit,
) : ListAdapter<GlobeModel, GlobeAdapter.GlobeViewHolder>(GlobeDiffUtil) {

    class GlobeViewHolder private constructor(
        private val binding: ItemGlobeBinding,
        private val onClickGlobeDetail: () -> Unit,
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
            fun from(parent: ViewGroup, onClickGlobeDetail: () -> Unit): GlobeViewHolder {
                return GlobeViewHolder(
                    ItemGlobeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    onClickGlobeDetail
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobeViewHolder {
        return GlobeViewHolder.from(parent, onClickGlobeDetail)
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