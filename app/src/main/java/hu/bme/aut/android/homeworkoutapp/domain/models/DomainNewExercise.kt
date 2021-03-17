package hu.bme.aut.android.homeworkoutapp.domain.models

import android.net.Uri
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import java.io.ByteArrayOutputStream

data class DomainNewExercise(
    val name: String = "",
    val reps: Int = 0,
    val duration: Duration = Duration(),
    val categoryValue: String = "",
    val videoUri: Uri? = null,
    val thumbnailInBytes: ByteArray? = null
)