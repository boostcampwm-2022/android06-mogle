package com.wakeup.presentation.adapter

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter("imageFromBitmap")
fun bindImageFromBitmap(view: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        Glide.with(view.context)
            .load(it)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}