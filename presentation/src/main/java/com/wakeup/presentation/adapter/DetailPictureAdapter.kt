package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ListItemDetailPictureBinding
import com.wakeup.presentation.databinding.ListItemDetailPictureEmptyBinding
import com.wakeup.presentation.model.PictureModel

class DetailPictureAdapter(
    private val onClickImageItem: (PictureModel) -> Unit,
) : ListAdapter<PictureModel, RecyclerView.ViewHolder>(DetailPictureDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DetailPictureViewHolder.from(parent, onClickImageItem)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DetailPictureViewHolder) {
            holder.bind(getItem(position))
        }
    }

    class DetailPictureViewHolder private constructor(
        private val binding: ListItemDetailPictureBinding,
        showDetailPicture: (PictureModel) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                binding.pictureModel?.let { picture ->
                    showDetailPicture(picture)
                }
            }
        }

        fun bind(pictureModel: PictureModel) {
            binding.pictureModel = pictureModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                onClickImageItem: (PictureModel) -> Unit,
            ): DetailPictureViewHolder {
                return DetailPictureViewHolder(
                    ListItemDetailPictureBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false),
                    onClickImageItem
                )
            }
        }
    }

    companion object DetailPictureDiffUtil : DiffUtil.ItemCallback<PictureModel>() {
        override fun areItemsTheSame(oldItem: PictureModel, newItem: PictureModel): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: PictureModel, newItem: PictureModel): Boolean {
            return oldItem == newItem
        }
    }
}