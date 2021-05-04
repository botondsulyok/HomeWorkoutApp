package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.UploadFailed
import hu.bme.aut.android.homeworkoutapp.ui.UploadSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import kotlinx.coroutines.delay
import javax.inject.Inject

class NewExerciseViewModel @Inject constructor(
    private val newExercisePresenter: NewExercisePresenter
) : NewExerciseViewModelBase() {

    override fun addExercise(exercise: UiNewExercise) = execute {
        viewState = Uploading
        when(val result = newExercisePresenter.addExercise(exercise)) {
            is ResultSuccess -> {
                postEvent(UploadSuccess("Created"))
            }
            is ResultFailure -> {
                postEvent(UploadFailed("Upload failed: ${result.reason.message.toString()}"))
                viewState = Initial
            }
        }
    }

}