package hu.bme.aut.android.homeworkoutapp.ui.newexercise.models

import hu.bme.aut.android.homeworkoutapp.utils.Duration

data class UiNewExercise(
    val name: String = "",
    val reps: Int = 0,
    val duration: Duration = Duration(),
    val category: String = "",
    val videoFilePath: String = ""
)