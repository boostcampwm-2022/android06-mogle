package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemPictureBinding
import com.wakeup.presentation.model.PictureModel

class PictureAdapter(private val onClickRemovePicture: (picture: PictureModel) -> Unit) :
    ListAdapter<PictureModel, PictureAdapter.PictureViewHolder>(PictureDiffUtil) {

    class PictureViewHolder private constructor(
        private val binding: ItemPictureBinding,
        private val onClickRemovePicture: (picture: PictureModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvRemove.setOnClickListener {
                binding.picture?.let { picture ->
                    onClickRemovePicture(picture)
                }
            }
        }

        fun bind(picture: PictureModel) {
            binding.picture = picture
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onClickRemovePicture: (picture: PictureModel) -> Unit,
            ): PictureViewHolder {
                return PictureViewHolder(
                    ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    onClickRemovePicture
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder.from(parent, onClickRemovePicture)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object PictureDiffUtil : DiffUtil.ItemCallback<PictureModel>() {
        override fun areItemsTheSame(oldItem: PictureModel, newItem: PictureModel): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: PictureModel, newItem: PictureModel): Boolean {
            return oldItem == newItem
        }
    }
}