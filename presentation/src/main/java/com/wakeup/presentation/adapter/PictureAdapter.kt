package com.wakeup.presentation.adapter

import android.graphics.Bitmap
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemPictureBinding

class PictureAdapter(private val onClickRemovePicture: (bitmap: Bitmap) -> Unit) :
    ListAdapter<Bitmap, PictureAdapter.ViewHolder>(diffCallback) {

    class ViewHolder private constructor(
        private val binding: ItemPictureBinding,
        private val onClickRemovePicture: (bitmap: Bitmap) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvRemove.setOnClickListener {
                binding.bitmap?.let { bitmap ->
                    onClickRemovePicture(bitmap)
                }
            }
        }

        fun bind(bitmap: Bitmap) {
            binding.bitmap = bitmap
        }

        companion object {
            fun from(
                parent: android.view.ViewGroup,
                onClickRemovePicture: (bitmap: Bitmap) -> Unit
            ) = ViewHolder(
                ItemPictureBinding.inflate(
                    android.view.LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                onClickRemovePicture
            )
        }
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, onClickRemovePicture)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        private val diffCallback =
            object : androidx.recyclerview.widget.DiffUtil.ItemCallback<Bitmap>() {
                override fun areItemsTheSame(oldItem: Bitmap, newItem: Bitmap) =
                    oldItem.generationId == newItem.generationId

                override fun areContentsTheSame(oldItem: Bitmap, newItem: Bitmap) =
                    oldItem.sameAs(newItem)
            }
    }
}