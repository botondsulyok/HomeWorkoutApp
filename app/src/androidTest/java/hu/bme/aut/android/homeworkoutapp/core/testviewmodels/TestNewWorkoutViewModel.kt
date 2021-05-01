package hu.bme.aut.android.homeworkoutapp.core.testviewmodels

import hu.bme.aut.android.homeworkoutapp.ui.newworkout.Initial
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.NewWorkoutViewModelBase
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.UploadSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout
import javax.inject.Inject

class TestNewWorkoutViewModel @Inject constructor() : NewWorkoutViewModelBase() {

    override fun addWorkout(workout: UiNewWorkout) {
        viewState = UploadSuccess
    }

    override fun toInitialState() {
        viewState = Initial
    }

}