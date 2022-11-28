package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemPlaceBinding
import com.wakeup.presentation.model.PlaceModel

class PlaceAdapter(private val onClick: (place: PlaceModel) -> Unit) :
    ListAdapter<PlaceModel, PlaceAdapter.PlaceViewHolder>(PlaceDiffUtil) {

    class PlaceViewHolder private constructor(
        private val binding: ItemPlaceBinding,
        private val onClick: (place: PlaceModel) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                binding.place?.let { place ->
                    onClick(place)
                }
            }
        }

        fun bind(place: PlaceModel) {
            binding.place = place
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, onClick: (place: PlaceModel) -> Unit): PlaceViewHolder {
                return PlaceViewHolder(
                    ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    onClick
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object PlaceDiffUtil : DiffUtil.ItemCallback<PlaceModel>() {
        override fun areItemsTheSame(oldItem: PlaceModel, newItem: PlaceModel): Boolean {
            return oldItem.detailAddress == newItem.detailAddress
        }

        override fun areContentsTheSame(oldItem: PlaceModel, newItem: PlaceModel): Boolean {
            return oldItem == newItem
        }
    }
}