package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise

abstract class NewExerciseViewModelBase : RainbowCakeViewModel<NewExerciseViewState>(Initial) {
    abstract fun addExercise(exercise: UiNewExercise)
}