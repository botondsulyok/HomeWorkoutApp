package hu.bme.aut.android.homeworkoutapp.ui.doingexercise

import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise

sealed class DoingExerciseViewState(val exercise: UiExercise)

class Initial(exercise: UiExercise): DoingExerciseViewState(exercise)

class Ready(exercise: UiExercise): DoingExerciseViewState(exercise)

class DoingExercise(exercise: UiExercise): DoingExerciseViewState(exercise)

class Finished(exercise: UiExercise): DoingExerciseViewState(exercise)

