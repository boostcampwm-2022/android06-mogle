package com.wakeup.presentation.lib.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.io.File
import kotlin.properties.Delegates

/**
 * 'glide' is a required ImageDialog.
 * 'PictureDialog' uses 'Glide v4.13.0'.
 */

class PictureDialog private constructor(context: Context) :
    BaseDialog<PictureDialog>(context) {

    data class Size(
        val width: Int,
        val height: Int
    )

    private lateinit var imageView: ImageView
    private lateinit var size: Size
    private var errorImageId by Delegates.notNull<Int>()


    companion object {
        private var INSTANCE: PictureDialog? = null

        /**
         * @param context 화면에 띄울 컨텍스트를 지정
         * @param layoutId 원하는 dialog 레이아웃을 넣어준다.
         * @param imageViewId dialog 레이아웃 내부의 imageView Id를 넣어준다.
         */
        fun with(context: Context, layoutId: Int, imageViewId: Int): PictureDialog {
            val instance = INSTANCE
            if (instance?.context == context && instance.baseLayoutId == layoutId) return instance

            INSTANCE = PictureDialog(context).apply {
                builder = AlertDialog.Builder(context)
                dialogView = LayoutInflater.from(context).inflate(layoutId, null)
                dialog = builder.setView(dialogView).create()
                imageView = dialogView.findViewById(imageViewId)
                baseLayoutId = layoutId
            }

            return INSTANCE ?: throw IllegalStateException("Instance is null.")
        }
    }

    fun setImageSize(width: Int, height: Int): PictureDialog {
        size = Size(width, height)
        return this
    }

    fun setErrorImage(id: Int): PictureDialog {
        errorImageId = id
        return this
    }


    fun setImagePath(
        filePath: String,
    ): PictureDialog {
        Glide.with(imageView.context)
            .load(File(filePath))
            .placeholder(errorImageId)
            .fallback(errorImageId)
            .error(errorImageId)
            .override(size.width, size.height)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
        return this
    }
}