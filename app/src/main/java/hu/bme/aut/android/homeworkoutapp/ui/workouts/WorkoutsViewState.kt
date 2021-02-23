package hu.bme.aut.android.homeworkoutapp.ui.workouts

import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.Workout

sealed class WorkoutsViewState

object WorkoutsLoading : WorkoutsViewState()

data class WorkoutsReady(val workoutsList: List<Workout> = listOf()) : WorkoutsViewState()
