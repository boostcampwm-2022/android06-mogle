package com.wakeup.presentation.adapter

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemPlaceBinding
import com.wakeup.presentation.model.PlaceModel

class PlaceAdapter(private val onClick: (place: PlaceModel) -> Unit) :
    ListAdapter<PlaceModel, PlaceAdapter.ViewHolder>(diffCallback) {

    class ViewHolder private constructor(
        private val binding: ItemPlaceBinding,
        private val onClick: (place: PlaceModel) -> Unit
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
        }

        companion object {
            fun from(
                parent: android.view.ViewGroup,
                onClick: (place: PlaceModel) -> Unit
            ) = ViewHolder(
                ItemPlaceBinding.inflate(
                    android.view.LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                onClick
            )
        }
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val diffCallback =
            object : androidx.recyclerview.widget.DiffUtil.ItemCallback<PlaceModel>() {
                override fun areItemsTheSame(oldItem: PlaceModel, newItem: PlaceModel) =
                    oldItem.detailAddress == newItem.detailAddress

                override fun areContentsTheSame(oldItem: PlaceModel, newItem: PlaceModel) =
                    oldItem == newItem
            }
    }
}