package hu.bme.aut.android.homeworkoutapp.ui.workouts

import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout

sealed class WorkoutsViewState

object Loading : WorkoutsViewState()

data class Loaded(val workoutsList: List<UiWorkout> = listOf()) : WorkoutsViewState()

data class LoadingFailed(val message: String) : WorkoutsViewState()
