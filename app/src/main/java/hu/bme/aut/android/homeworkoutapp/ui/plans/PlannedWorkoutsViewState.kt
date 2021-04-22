package hu.bme.aut.android.homeworkoutapp.ui.plans

import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout

sealed class PlannedWorkoutsViewState

object PlannedWorkoutsLoading : PlannedWorkoutsViewState()

data class PlannedWorkoutsLoaded(val workoutsList: List<UiWorkout> = listOf()) : PlannedWorkoutsViewState()

data class PlannedWorkoutsFailed(val message: String) : PlannedWorkoutsViewState()