package hu.bme.aut.android.homeworkoutapp.ui.workout

import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise

sealed class WorkoutViewState

object Loading : WorkoutViewState()

data class Loaded(val exercisesList: List<UiExercise> = listOf()) : WorkoutViewState()

data class Failed(val message: String) : WorkoutViewState()