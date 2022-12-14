package com.wakeup.data.util

import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wakeup.data.BuildConfig
import com.wakeup.data.database.entity.PictureEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class InternalFileUtil @Inject constructor(
    private val context: Context,
) {

    fun savePictureInInternalStorage(picture: PictureEntity) {
        Timber.d("picture uri: ${picture.path.toUri()}")
        if (picture.path.startsWith(context.filesDir.path)) return
        Glide.with(context)
            .asBitmap()
            .load(picture.path.toUri())
            .listener(PictureSaveRequestListener(picture, context, ::catchException))
            .override(1000, 1000)
            .fitCenter()
            .submit()
    }

    fun deletePictureInInternalStorage(fileName: String) {
        runCatching {
            val filePath = "${context.filesDir.path}/$DIR_NAME/$fileName"
            val file = File(filePath)
            if (file.exists()) {
                Timber.d("사진 삭제: $fileName")
                file.delete()
            }
        }.onFailure { catchException(it) }
    }

    class PictureSaveRequestListener(
        private val picture: PictureEntity,
        private val context: Context,
        private val catchException: (Throwable) -> Unit
    ) : RequestListener<Bitmap> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Bitmap>?,
            isFirstResource: Boolean,
        ): Boolean {
            Timber.e(e)
            return false
        }

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any?,
            target: Target<Bitmap>?,
            dataSource: DataSource?,
            isFirstResource: Boolean,
        ): Boolean {
            CoroutineScope(Dispatchers.IO).launch {
                runCatching {
                    val dirPath = File(context.filesDir, DIR_NAME).apply { mkdirs() }
                    val filePath = File("${dirPath}/${picture.path.substringAfterLast("/")}")
                    FileOutputStream(filePath).use { out ->
                        resource?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }
                }.onFailure { catchException(it) }
            }
            return false
        }
    }

    private fun catchException(exception: Throwable) {
        when (exception) {
            is FileNotFoundException ->
                Timber.e("FileNotFoundException : " + exception.message)
            is IOException ->
                Timber.e("IOException : " + exception.message)
            else ->
                Timber.e("AnotherException : " + exception.message)
        }
    }

    companion object {
        private const val DIR_NAME = "images"
    }
}