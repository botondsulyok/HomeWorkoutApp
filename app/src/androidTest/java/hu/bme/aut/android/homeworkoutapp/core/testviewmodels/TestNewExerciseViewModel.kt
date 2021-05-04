package hu.bme.aut.android.homeworkoutapp.core.testviewmodels

import hu.bme.aut.android.homeworkoutapp.ui.UploadSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.Initial
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.NewExerciseViewModelBase
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import javax.inject.Inject

class TestNewExerciseViewModel @Inject constructor() : NewExerciseViewModelBase() {

    override fun addExercise(exercise: UiNewExercise) {
        postEvent(UploadSuccess())
    }

}