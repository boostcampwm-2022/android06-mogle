package com.wakeup.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.domain.model.Picture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.security.DigestException
import java.security.MessageDigest
import javax.inject.Inject

class InternalFileUtil @Inject constructor(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) {

    suspend fun savePictureInInternalStorageAndGetFileName(pictures: List<Picture>): List<PictureEntity> {
        return withContext(dispatcher) {
            val pictureFiles = mutableListOf<String>()
            pictures.forEach { picture ->
                val fileName = hashSHA256(encodeBase64(picture.bitmap))
                val tempFile = File(context.filesDir, fileName)

                runCatching {
                    tempFile.createNewFile()
                }.onFailure { e ->
                    Timber.e("MyTag", "FileNotFoundException : " + e.message)
                }.onSuccess {
                    runCatching {
                        val out = FileOutputStream(tempFile)
                        out.write(picture.bitmap)
                        out.close()
                    }.onFailure { e ->
                        Timber.e("MyTag", "IOException : " + e.message)
                    }.onSuccess {
                        pictureFiles.add(fileName)
                    }
                }
            }
            pictureFiles.map { PictureEntity(fileName = it) }
        }
    }

    suspend fun getPictureInInternalStorage(pictures: List<PictureEntity>, thumbnailId: Long?): List<Picture> {
        return withContext(dispatcher) {
            val pictureBitmaps = ArrayDeque<ByteArray>()

            pictures.forEach { picture ->
                runCatching {
                    BitmapFactory.decodeFile("${context.filesDir}/${picture.fileName}")
                }.onFailure { e ->
                    Timber.e("MyTag", "FileNotFoundException : " + e.message)
                }.onSuccess { bitmapPicture ->
                    runCatching {
                        ByteArrayOutputStream()
                    }.onFailure { e ->
                        Timber.e("MyTag", "IOException : " + e.message)
                    }.onSuccess { stream ->
                        bitmapPicture.compress(Bitmap.CompressFormat.JPEG, 1, stream)
                        // thumbnail 사진이 가장 앞으로 오도록, pictureBitmaps[0]는 항상 thumbnail 사진
                        if(thumbnailId != null && picture.id == thumbnailId) {
                            pictureBitmaps.addFirst(stream.toByteArray())
                        } else {
                            pictureBitmaps.addLast(stream.toByteArray())
                        }
                    }
                }
            }
            pictureBitmaps.map { Picture(bitmap = it) }
        }
    }

    private fun encodeBase64(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun hashSHA256(msg: String): String {
        val hash: ByteArray
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(msg.toByteArray())
            hash = md.digest()
        } catch (e: CloneNotSupportedException) {
            throw DigestException("couldn't make digest of patial content")
        }
        return bytesToHex(hash)
    }

    private fun bytesToHex(byteArray: ByteArray): String {

        val hexChars = CharArray(byteArray.size * 2)
        for (i in byteArray.indices) {
            val v = byteArray[i].toInt() and 0xff
            hexChars[i * 2] = DIGITS[v shr 4]
            hexChars[i * 2 + 1] = DIGITS[v and 0xf]
        }
        return String(hexChars)
    }

    companion object {
        private const val DIGITS = "0123456789ABCDEF"
    }
}

