package com.wakeup.presentation.mapper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.wakeup.domain.model.Picture
import com.wakeup.presentation.model.PictureModel
import java.io.ByteArrayOutputStream

fun PictureModel.toDomain(): Picture {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 1, stream)
    return Picture(stream.toByteArray())
}

fun Picture.toPresentation(): PictureModel {
    return PictureModel(
        bitmap = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.size)
    )
}
