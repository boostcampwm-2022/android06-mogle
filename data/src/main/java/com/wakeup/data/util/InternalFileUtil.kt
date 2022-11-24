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
                println(picture)
                val fileName = hashSHA256(encodeBase64(picture.bitmap))
                val tempFile = File(context.filesDir, fileName)

                try {
                    tempFile.createNewFile()
                    val out = FileOutputStream(tempFile)
                    val convertedBitmap =
                        BitmapFactory.decodeByteArray(picture.bitmap, 0, picture.bitmap.size)
                    convertedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    out.close()
                } catch (e: FileNotFoundException) {
                    Timber.e("MyTag", "FileNotFoundException : " + e.message)
                } catch (e: IOException) {
                    Timber.e("MyTag", "IOException : " + e.message)
                }
                pictureFiles.add(fileName)
            }
            pictureFiles.map { PictureEntity(fileName = it) }
        }
    }

    suspend fun getPictureInInternalStorage(pictures: List<PictureEntity>): List<Picture> {
        return withContext(dispatcher) {
            val pictureBitmaps = mutableListOf<ByteArray>()

            val files: Array<String> = context.fileList()
            pictures.forEach { pictureFileName ->
                files.forEach { savedFileName ->
                    Timber.d("MyTag", savedFileName)
                    println(savedFileName)
                    println(pictureFileName.fileName)
                    if (savedFileName == pictureFileName.fileName) {
                        val bitmapPicture =
                            BitmapFactory.decodeFile("${context.filesDir}/${savedFileName}")
                        val stream = ByteArrayOutputStream()
                        bitmapPicture.compress(Bitmap.CompressFormat.PNG, 1, stream)
                        pictureBitmaps.add(stream.toByteArray())
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
        } catch (e: CloneNotSupportedException){
            throw DigestException("couldn't make digest of patial content")
        }
        return bytesToHex(hash)
    }

    private val digits = "0123456789ABCDEF"

    fun bytesToHex(byteArray: ByteArray): String {
        val hexChars = CharArray(byteArray.size * 2)
        for (i in byteArray.indices) {
            val v = byteArray[i].toInt() and 0xff
            hexChars[i * 2] = digits[v shr 4]
            hexChars[i * 2 + 1] = digits[v and 0xf]
        }
        return String(hexChars)
    }
}

