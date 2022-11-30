package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemPlaceBinding
import com.wakeup.presentation.model.PlaceModel
import com.wakeup.presentation.ui.addmoment.PlaceSearchFragmentDirections

class PlaceAdapter : ListAdapter<PlaceModel, PlaceAdapter.PlaceViewHolder>(PlaceDiffUtil) {

    class PlaceViewHolder private constructor(
        private val binding: ItemPlaceBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                navigateToPlaceCheckFragment()
            }
        }

        private fun navigateToPlaceCheckFragment() {
            val action =
                PlaceSearchFragmentDirections.actionPlaceSearchToPlaceCheck(binding.place ?: return)
            itemView.findNavController().navigate(action)
        }

        fun bind(place: PlaceModel) {
            binding.place = place
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): PlaceViewHolder {
                return PlaceViewHolder(
                    ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder.from(parent)
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