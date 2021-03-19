package hu.bme.aut.android.homeworkoutapp.domain.models

import hu.bme.aut.android.homeworkoutapp.utils.Duration

data class DomainExercise(
        val id: String = "",
        val name: String = "",
        val reps: Int = 0,
        val duration: Duration = Duration(),
        val categoryValue: String = "",
        val videoUrl: String = "",
        val videoLength: Duration = Duration(),
        val thumbnailUrl: String = ""
)