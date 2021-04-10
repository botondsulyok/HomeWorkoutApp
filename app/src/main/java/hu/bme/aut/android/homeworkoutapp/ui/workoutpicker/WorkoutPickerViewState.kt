package hu.bme.aut.android.homeworkoutapp.ui.workoutpicker

import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout

sealed class WorkoutPickerViewState

object Loading : WorkoutPickerViewState()

data class Loaded(val workoutsList: List<UiWorkout> = listOf()) : WorkoutPickerViewState()

object Ready : WorkoutPickerViewState()

data class Failed(val message: String) : WorkoutPickerViewState()

object Uploading : WorkoutPickerViewState()

data class UploadFailed(val message: String) : WorkoutPickerViewState()

object UploadSuccess : WorkoutPickerViewState()