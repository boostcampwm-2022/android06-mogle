package com.wakeup.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ListItemDetailPictureBinding
import com.wakeup.presentation.databinding.ListItemDetailPictureEmptyBinding
import com.wakeup.presentation.model.PictureModel

class DetailPictureAdapter :
    ListAdapter<PictureModel, RecyclerView.ViewHolder>(DetailPictureDiffUtil) {

    private val pictureType = 0
    private val emptyType = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == pictureType) {
            DetailPictureViewHolder.from(parent)
        } else {
            EmptyViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DetailPictureViewHolder) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList.isEmpty()) emptyType else pictureType
    }

    // ItemCount가 0이면 onCreateViewHolder()가 호출되지 않아, EmptyViewHolder가 생성되지 않습니다.
    // 따라서, 리스트가 비어있어도 아이템이 1개 있는 것처럼 처리합니다.
    override fun getItemCount(): Int {
        return if (currentList.isEmpty()) 1 else currentList.size
    }

    class DetailPictureViewHolder private constructor(private val binding: ListItemDetailPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pictureModel: PictureModel) {
            binding.pictureModel = pictureModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): DetailPictureViewHolder {
                return DetailPictureViewHolder(
                    ListItemDetailPictureBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    class EmptyViewHolder private constructor(binding: ListItemDetailPictureEmptyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): EmptyViewHolder {
                return EmptyViewHolder(
                    ListItemDetailPictureEmptyBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
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