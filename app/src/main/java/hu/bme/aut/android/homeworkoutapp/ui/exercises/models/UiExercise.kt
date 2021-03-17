package hu.bme.aut.android.homeworkoutapp.ui.exercises.models

import hu.bme.aut.android.homeworkoutapp.utils.Duration

data class UiExercise(
        val id: String = "",
        val name: String = "",
        val reps: Int = 0,
        val duration: Duration = Duration(),
        val categoryEntry: String = "",
        val videoUrl: String = "",
        val thumbnailUrl: String = ""
)