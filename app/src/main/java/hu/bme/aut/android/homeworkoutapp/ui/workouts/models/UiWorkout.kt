package hu.bme.aut.android.homeworkoutapp.ui.workouts.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiWorkout(
        val id: String = "",
        val name: String = ""
) : Parcelable