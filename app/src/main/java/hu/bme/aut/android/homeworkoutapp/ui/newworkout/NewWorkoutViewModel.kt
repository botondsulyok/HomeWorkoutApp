package hu.bme.aut.android.homeworkoutapp.ui.newworkout

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.UploadFailed
import hu.bme.aut.android.homeworkoutapp.ui.UploadSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout
import javax.inject.Inject

class NewWorkoutViewModel @Inject constructor(
    private val newWorkoutPresenter: NewWorkoutPresenter
) : NewWorkoutViewModelBase() {

    override fun addWorkout(workout: UiNewWorkout) = execute {
        viewState = Uploading
        when(val result = newWorkoutPresenter.addWorkout(workout)) {
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