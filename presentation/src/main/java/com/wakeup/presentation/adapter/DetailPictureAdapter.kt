package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ListItemDetailPictureBinding
import com.wakeup.presentation.model.PictureModel

class DetailPictureAdapter :
    ListAdapter<PictureModel, DetailPictureAdapter.DetailPictureViewHolder>(DetailPictureDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailPictureViewHolder {
        return DetailPictureViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DetailPictureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DetailPictureViewHolder private constructor(private val binding: ListItemDetailPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pictureModel: PictureModel) {
            binding.pictureModel = pictureModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): DetailPictureViewHolder = DetailPictureViewHolder(
                ListItemDetailPictureBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false))
        }
    }

    companion object DetailPictureDiffUtil : DiffUtil.ItemCallback<PictureModel>() {
        override fun areItemsTheSame(oldItem: PictureModel, newItem: PictureModel): Boolean {
            return oldItem.bitmap.sameAs(newItem.bitmap)
        }

        override fun areContentsTheSame(oldItem: PictureModel, newItem: PictureModel): Boolean {
            return oldItem == newItem
        }
    }
}