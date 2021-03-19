package hu.bme.aut.android.homeworkoutapp.ui.newexercise.models

import android.net.Uri
import android.os.Parcelable
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import kotlinx.android.parcel.Parcelize
import java.io.ByteArrayOutputStream

@Parcelize
data class UiNewExercise(
    val name: String = "",
    val reps: Int = 0,
    val duration: Duration = Duration(),
    val categoryEntry: String = "",
    val videoUri: Uri? = null,
    val videoLength: Duration = Duration()
) : Parcelable