package hu.bme.aut.android.homeworkoutapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class MediaProcessor @Inject constructor(val context: Context) {

    fun createThumbnailFromVideo(videoUri: Uri): ByteArray {
        val bitmap = Glide.with(context).asBitmap().load(videoUri).submit().get()
        return compressBitmap(bitmap)
    }

    fun compressBitmap(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)
        val imageInBytes = baos.toByteArray()
        return imageInBytes
    }

}