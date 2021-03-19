package hu.bme.aut.android.homeworkoutapp.ui.exercises.models

import android.os.Parcelable
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiExercise(
        val id: String = "",
        val name: String = "",
        val reps: Int = 0,
        val duration: Duration = Duration(),
        val categoryEntry: String = "",
        val videoUrl: String = "",
        val videoLength: Duration = Duration(),
        val thumbnailUrl: String = ""
) : Parcelable