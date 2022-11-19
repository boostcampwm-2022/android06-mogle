package com.wakeup.presentation.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.wakeup.domain.model.Moment
import java.io.ByteArrayOutputStream

fun MomentModel.toDomain(): Moment = Moment(
    id = id,
    mainAddress = mainAddress,
    detailAddress = detailAddress,
    images = bitmapImages?.map { it.toByteArray() },
    content = content,
    globes = globes,
    date = date
)

private fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 90, stream)
    return stream.toByteArray()
}

fun Moment.toPresentation(): MomentModel = MomentModel(
    id = id,
    mainAddress = mainAddress,
    detailAddress = detailAddress,
    bitmapImages = images?.map { BitmapFactory.decodeByteArray(it, 0, it.size) },
    content = content,
    globes = globes,
    date = date
)