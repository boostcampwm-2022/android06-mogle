package com.wakeup.presentation.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.wakeup.presentation.R
import com.wakeup.presentation.model.GlobeModel
import com.wakeup.presentation.model.WeatherModel
import com.wakeup.presentation.ui.UiState
import timber.log.Timber
import java.io.File


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

@BindingAdapter("contentImageFromFilePath")
fun bindContentImageFromFile(view: ImageView, filePath: String?) {
    bindImageFromFile(view, filePath, 1000, 1000)
}

@BindingAdapter("thumbnailImageFromFilePath")
fun bindThumbnailImageFromFile(view: ImageView, filePath: String?) {
    bindImageFromFile(view, filePath, 200, 200)
}

fun bindImageFromFile(view: ImageView, filePath: String?, width: Int, height: Int) {
    val url = "${view.context.filesDir}/" + "images/" + "$filePath"
    Timber.d(url)
    Glide.with(view.context)
        .load(File(url))
        .placeholder(R.drawable.ic_no_image)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean,
            ): Boolean {
                view.setImageResource(R.drawable.ic_no_image)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean,
            ): Boolean {
                view.setImageDrawable(resource)
                return true
            }
        })
        .fallback(R.drawable.ic_no_image)
        .error(R.drawable.ic_no_image)
        .override(width, height)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("imageFromBitmap")
fun bindImageFromBitmap(view: ImageView, bitmap: Bitmap?) {
    Glide.with(view.context)
        .load(bitmap)
        .fallback(R.drawable.ic_no_image)
        .error(R.drawable.ic_no_image)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, url: String?) {
    url?.let { s ->
        if (s.startsWith("content://").not()) {
            bindThumbnailImageFromFile(view, s)
            return
        }
    }

    Glide.with(view.context)
        .load(url)
        .fallback(R.drawable.ic_no_image)
        .error(R.drawable.ic_no_image)
        .transition(DrawableTransitionOptions.withCrossFade())
        .override(200, 200)
        .into(view)
}

@BindingAdapter("globeNames")
fun bindGlobeNames(view: TextView, globes: List<GlobeModel>?) {
    globes ?: return
    val res = view.resources

    view.text = if (globes.isEmpty()) {
        res.getString(R.string.moment_detail_no_globe)
    } else if (globes.size == 1) {
        globes.first().name
    } else {
        String.format(
            res.getString(R.string.moment_detail_globes),
            globes.first().name,
            globes.size - 1
        )
    }
}

@BindingAdapter("globeItems")
fun bindGlobeItems(view: MaterialAutoCompleteTextView, items: List<GlobeModel>) {
    view.setSimpleItems(items.map { it.name }.toTypedArray())
}

@BindingAdapter("weatherImage")
fun bindWeatherImage(view: ImageView, uiState: UiState<WeatherModel>) {
    if (uiState is UiState.Success) {
        bindImageFromUrl(view, uiState.item.iconUrl)
    }
}

@BindingAdapter("temperatureText")
fun bindTemperatureText(view: TextView, uiState: UiState<WeatherModel>) {
    if (uiState is UiState.Success) {
        view.text = view.resources.getString(R.string.temperature_celsius, uiState.item.temperature)
    }
}

@BindingAdapter("showOnLoading")
fun bindShowingOnLoading(view: View, uiState: UiState<Any>) {
    view.isVisible = uiState is UiState.Loading
}

@BindingAdapter("hideOnLoading")
fun bindHidingOnLoading(view: View, uiState: UiState<Any>) {
    view.isVisible =
        !(uiState is UiState.Loading || uiState is UiState.Failure || uiState is UiState.Empty)
}

@BindingAdapter("showOnFailure")
fun bindShowingOnFailure(view: View, uiState: UiState<Any>) {
    view.isVisible = uiState is UiState.Failure
}
