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
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.wakeup.presentation.R
import com.wakeup.presentation.extension.getBitMapFromVectorDrawable
import com.wakeup.presentation.model.GlobeModel
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
    // TODO fallback 이미지 vector와 png 같이 쓸건지 고민 - 벡터 변환 함수가 필요해서.
    view.adapter?.let {
        itemList?.let { itemList ->
            if (itemList.isEmpty()) {
                val fallbackBitmap =
                    getBitMapFromVectorDrawable(view.context, R.drawable.ic_no_image)
                //(view.adapter as ListAdapter<Any, *>).submitList(listOf(PictureModel(fallbackBitmap)))
            } else {
                (view.adapter as ListAdapter<Any, *>).submitList(itemList)
            }
        }
    } ?: run {
        val fallbackBitmap = getBitMapFromVectorDrawable(view.context, R.drawable.ic_no_image)
        //(view.adapter as ListAdapter<Any, *>).submitList(listOf(PictureModel(fallbackBitmap)))
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
    Timber.d(filePath)
    val url = "${view.context.filesDir}/" + "images/" + "$filePath"
    Timber.d(url)
    Glide.with(view.context)
        .load(File(url))
        .fallback(R.drawable.ic_no_image)
        .timeout(500)
        .override(width, height)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("imageFromBitmap")
fun bindImageFromBitmap(view: ImageView, bitmap: Bitmap?) {
    Glide.with(view.context)
        .load(bitmap)
        .fallback(R.drawable.ic_no_image)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, url: String?) {
    url?.let {
        Glide.with(view.context)
            .load(it)
            .transition(DrawableTransitionOptions.withCrossFade())
            .override(200, 200)
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

@BindingAdapter("globeItems")
fun bindGlobeItems(view: MaterialAutoCompleteTextView, items: List<GlobeModel>) {
    view.setSimpleItems(items.map { it.name }.toTypedArray())
}
