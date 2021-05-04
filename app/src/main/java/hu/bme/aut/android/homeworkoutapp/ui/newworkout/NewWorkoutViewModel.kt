package hu.bme.aut.android.homeworkoutapp.ui.newworkout

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.UploadFailed
import hu.bme.aut.android.homeworkoutapp.ui.UploadSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout
import hu.bme.aut.android.homeworkoutapp.utils.ResourcesHelper
import javax.inject.Inject

class NewWorkoutViewModel @Inject constructor(
    private val newWorkoutPresenter: NewWorkoutPresenter,
    private val resourcesHelper: ResourcesHelper
) : NewWorkoutViewModelBase() {

    override fun addWorkout(workout: UiNewWorkout) = execute {
        viewState = Uploading
        when(val result = newWorkoutPresenter.addWorkout(workout)) {
            is ResultSuccess -> {
                postEvent(UploadSuccess(resourcesHelper.getString(R.string.txt_created)))
            }
            is ResultFailure -> {
                postEvent(UploadFailed("${resourcesHelper.getString(R.string.txt_upload_failed)}: ${result.reason.message.toString()}"))
                viewState = Initial
            }
        }
    }

}