package hu.bme.aut.android.homeworkoutapp.ui.workouts

import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.Workout

sealed class WorkoutsViewState

object Loading : WorkoutsViewState()

data class Ready(val workoutsList: List<Workout> = listOf()) : WorkoutsViewState()
