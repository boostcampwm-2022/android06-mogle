package com.wakeup.presentation.lib.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.io.File

/**
 * 'glide' is a required ImageDialog.
 * 'PictureDialog' uses 'Glide v4.13.0'.
 */

class PictureDialog private constructor(context: Context) :
    BaseDialog<PictureDialog>(context) {

    private lateinit var imageView: ImageView

    companion object {
        /**
         * @param context 화면에 띄울 컨텍스트를 지정
         * @param layoutId 원하는 dialog 레이아웃을 넣어준다.
         * @param imageViewId dialog 레이아웃 내부의 imageView Id를 넣어준다.
         */
        fun with(context: Context, layoutId: Int, imageViewId: Int): PictureDialog {
            return PictureDialog(context).apply {
                builder = AlertDialog.Builder(context)
                dialogView = LayoutInflater.from(context).inflate(layoutId, null)
                dialog = builder.setView(dialogView).create()
                imageView = dialogView.findViewById(imageViewId)
            }
        }
    }

    fun setImageFilePath(
        filePath: String,
        errorImageDrawableId: Int,
        width: Int,
        height: Int,
    ): PictureDialog {
        Glide.with(imageView.context)
            .load(File(filePath))
            .placeholder(errorImageDrawableId)
            .fallback(errorImageDrawableId)
            .error(errorImageDrawableId)
            .override(width, height)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
        return this
    }
}