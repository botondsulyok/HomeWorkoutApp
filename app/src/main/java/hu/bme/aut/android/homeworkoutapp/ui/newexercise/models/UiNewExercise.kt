package hu.bme.aut.android.homeworkoutapp.ui.newexercise.models

import android.net.Uri
import android.os.Parcelable
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiNewExercise(
    val name: String = "",
    val reps: Int = 0,
    val duration: Duration = Duration(),
    val categoryEntry: String = "",
    val videoUri: Uri? = null,
    val videoLengthInMilliseconds: Int = 0
) : Parcelable