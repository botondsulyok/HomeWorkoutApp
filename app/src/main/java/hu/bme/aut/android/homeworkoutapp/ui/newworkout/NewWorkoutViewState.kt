package hu.bme.aut.android.homeworkoutapp.ui.newworkout

sealed class NewWorkoutViewState

object Ready : NewWorkoutViewState()

object Uploading : NewWorkoutViewState()