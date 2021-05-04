package hu.bme.aut.android.homeworkoutapp.ui.newworkout

sealed class NewWorkoutViewState

object Initial : NewWorkoutViewState()

object Uploading : NewWorkoutViewState()

