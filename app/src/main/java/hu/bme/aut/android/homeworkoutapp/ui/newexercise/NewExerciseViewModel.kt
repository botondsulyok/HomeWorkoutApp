package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import javax.inject.Inject

class NewExerciseViewModel @Inject constructor(
    private val newExercisePresenter: NewExercisePresenter
) : RainbowCakeViewModel<NewExerciseViewState>(Initial) {

    fun addExercise(exercise: UiNewExercise) = execute {
        viewState = Uploading
        val result = newExercisePresenter.addExercise(exercise)
        viewState = when(result) {
            is ResultSuccess -> {
                UploadSuccess
            }
            is ResultFailure -> {
                UploadFailed(result.reason.message.toString())
            }
        }
    }

}