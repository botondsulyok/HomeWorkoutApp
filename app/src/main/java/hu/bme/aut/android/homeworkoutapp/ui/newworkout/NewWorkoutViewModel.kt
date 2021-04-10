package hu.bme.aut.android.homeworkoutapp.ui.newworkout

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout
import javax.inject.Inject

class NewWorkoutViewModel @Inject constructor(
    private val newWorkoutPresenter: NewWorkoutPresenter
) : RainbowCakeViewModel<NewWorkoutViewState>(Initial) {

    fun addWorkout(workout: UiNewWorkout) = execute {
        viewState = Uploading
        val result = newWorkoutPresenter.addWorkout(workout)
        viewState = when(result) {
            is ResultSuccess -> {
                UploadSuccess
            }
            is ResultFailure -> {
                UploadFailed(result.reason.message.toString())
            }
        }
    }

    fun toInitialState() {
        viewState = Initial
    }

}