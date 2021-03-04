package hu.bme.aut.android.homeworkoutapp.ui.exercises

import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise


sealed class ExercisesViewState

object Loading : ExercisesViewState()

data class Loaded(val exercisesList: List<UiExercise> = listOf()) : ExercisesViewState()

data class Failed(val message: String) : ExercisesViewState()
