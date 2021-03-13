package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise

sealed class NewExerciseViewState

object Initial : NewExerciseViewState()

object Uploading : NewExerciseViewState()

data class UploadFailed(val message: String) : NewExerciseViewState()

object UploadSuccess : NewExerciseViewState()