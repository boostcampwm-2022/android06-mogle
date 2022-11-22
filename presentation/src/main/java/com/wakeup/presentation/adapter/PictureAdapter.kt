package com.wakeup.presentation.adapter

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wakeup.presentation.databinding.ItemPictureBinding
import com.wakeup.presentation.model.PictureModel

class PictureAdapter(private val onClickRemovePicture: (picture: PictureModel) -> Unit) :
    ListAdapter<PictureModel, PictureAdapter.ViewHolder>(diffCallback) {

    class ViewHolder private constructor(
        private val binding: ItemPictureBinding,
        private val onClickRemovePicture: (picture: PictureModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

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
                parent: android.view.ViewGroup,
                onClickRemovePicture: (picture: PictureModel) -> Unit
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
            object : androidx.recyclerview.widget.DiffUtil.ItemCallback<PictureModel>() {
                override fun areItemsTheSame(oldItem: PictureModel, newItem: PictureModel) =
                    oldItem.bitmap == newItem.bitmap

                override fun areContentsTheSame(oldItem: PictureModel, newItem: PictureModel) =
                    oldItem == newItem
            }
    }
}