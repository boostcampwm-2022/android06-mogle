package com.wakeup.presentation.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("submitList")
    fun bindSubmitList(view: RecyclerView, itemList: List<Any>?) {
        view.adapter?.let {
            (view.adapter as ListAdapter<Any, *>).submitList(itemList)
        }
    }

    @JvmStatic
    @BindingAdapter("imageBitmap")
    fun bindImageBitmap(view: android.widget.ImageView, bitmap: android.graphics.Bitmap?) {
        view.setImageBitmap(bitmap)
    }
}