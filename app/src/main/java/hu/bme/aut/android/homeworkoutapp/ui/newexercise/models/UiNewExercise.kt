package hu.bme.aut.android.homeworkoutapp.ui.newexercise.models

import android.net.Uri
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import java.io.ByteArrayOutputStream

data class UiNewExercise(
    val name: String = "",
    val reps: Int = 0,
    val duration: Duration = Duration(),
    val categoryEntry: String = "",
    val videoUri: Uri? = null
)