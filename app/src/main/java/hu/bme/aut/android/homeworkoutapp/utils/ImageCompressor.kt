package hu.bme.aut.android.homeworkoutapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ImageCompressor @Inject constructor(val context: Context) {

    fun compressImage(videoUri: Uri): ByteArray {
        val bitmap = Glide.with(context).asBitmap().load(videoUri).submit().get()
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos)
        val imageInBytes = baos.toByteArray()
        return imageInBytes
    }

}