package hu.bme.aut.android.homeworkoutapp.core.testviewmodels

import hu.bme.aut.android.homeworkoutapp.ui.newexercise.Initial
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.NewExerciseViewModelBase
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.UploadSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import javax.inject.Inject

class TestNewExerciseViewModel @Inject constructor() : NewExerciseViewModelBase() {

    override fun addExercise(exercise: UiNewExercise) {
        viewState = UploadSuccess
    }

    override fun toInitialState() {
        viewState = Initial
    }

}