package com.wakeup.presentation.adapter

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.wakeup.presentation.R
import com.wakeup.presentation.model.GlobeModel


@BindingAdapter("submitList")
fun bindSubmitList(view: RecyclerView, itemList: List<Any>?) {
    view.adapter?.let {
        (view.adapter as ListAdapter<Any, *>).submitList(itemList)
    }
}

@BindingAdapter("submitList")
fun bindSubmitList(view: ViewPager2, itemList: List<Any>?) {
    view.adapter?.let {
        (view.adapter as ListAdapter<Any, *>).submitList(itemList)
    }
}

@BindingAdapter("imageBitmap")
fun bindImageBitmap(view: ImageView, bitmap: Bitmap?) {
    view.setImageBitmap(bitmap)
}

@BindingAdapter("isGone")
fun bindGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("imageFromBitmap")
fun bindImageFromBitmap(view: ImageView, bitmap: Bitmap?) {
    Glide.with(view.context)
        .load(bitmap)
        .fallback(R.drawable.sample_image2)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, url: String?) {
    url?.let {
        Glide.with(view.context)
            .load(it)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("globeNames")
fun bindGlobeNames(view: TextView, globes: List<GlobeModel>) {
    val res = view.resources

    view.text = if (globes.isEmpty()) {
        res.getString(R.string.moment_detail_no_globe)
    } else if (globes.size == 1) {
        globes.first().name
    } else {
        String.format(res.getString(R.string.moment_detail_globes),
            globes.first().name,
            globes.size - 1)
    }
}