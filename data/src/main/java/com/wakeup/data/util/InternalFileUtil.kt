package com.wakeup.data.util

import android.content.Context
import android.util.Base64
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.domain.model.Picture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
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
                    FileOutputStream(tempFile).use { out ->
                        out.write(picture.bitmap)
                        out.close()
                    }
                }.onSuccess {
                    pictureFiles.add(fileName)
                }.onFailure { exception ->
                    when (exception) {
                        is FileNotFoundException ->
                            Timber.e("MyTag", "FileNotFoundException : " + exception.message)
                        is IOException ->
                            Timber.e("MyTag", "IOException : " + exception.message)
                        else ->
                            Timber.e("MyTag", "AnotherException : " + exception.message)
                    }
                }
            }
            pictureFiles.map { PictureEntity(fileName = it) }
        }
    }


    suspend fun getPictureInInternalStorage(
        pictures: List<PictureEntity>,
        thumbnailId: Long?,
    ): List<Picture> {
        return withContext(dispatcher) {
            val pictureBitmaps = ArrayDeque<ByteArray>()

            pictures.forEach { picture ->
                runCatching {
                    val file = File("${context.filesDir}/${picture.fileName}")
                    val buffer = BufferedInputStream(FileInputStream(file))
                    val bytes = ByteArray(file.length().toInt())
                    buffer.read(bytes, 0, bytes.size)
                    buffer to bytes
                }.onSuccess { (buffer, bytes) ->
                    // thumbnail 사진이 가장 앞으로 오도록, pictureBitmaps[0]는 항상 thumbnail 사진
                    if (thumbnailId != null && picture.id == thumbnailId) {
                        pictureBitmaps.addFirst(bytes)
                    } else {
                        pictureBitmaps.addLast(bytes)
                    }
                    buffer.close()
                }.onFailure { exception ->
                    when (exception) {
                        is FileNotFoundException ->
                            Timber.e("MyTag", "FileNotFoundException : " + exception.message)
                        is IOException ->
                            Timber.e("MyTag", "IOException : " + exception.message)
                        else ->
                            Timber.e("MyTag", "AnotherException : " + exception.message)
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
        return runCatching {
            val hash: ByteArray
            val md = MessageDigest.getInstance("SHA-256")
            md.update(msg.toByteArray())
            hash = md.digest()
            bytesToHex(hash)
        }.onFailure { exception ->
            if (exception is CloneNotSupportedException) {
                Timber.e("MyTag", "\"couldn't make digest of patial content\" DigestException : " + exception.message)
                throw DigestException()
            } else {
                Timber.e("MyTag", "AnotherException : " + exception.message)
                throw Exception()
            }
        }.getOrThrow()
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

