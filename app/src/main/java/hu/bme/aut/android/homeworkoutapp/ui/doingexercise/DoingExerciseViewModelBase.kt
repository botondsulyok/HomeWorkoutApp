package hu.bme.aut.android.homeworkoutapp.ui.doingexercise

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise

abstract class DoingExerciseViewModelBase :
    RainbowCakeViewModel<DoingExerciseViewState>(Initial(UiExercise())) {
    abstract var exercises: List<UiExercise>
    abstract var currentExerciseNumber: Int
    abstract var initialDurationInMilliseconds: Int
    abstract var initialReps: Int

    abstract fun addExercises(e: List<UiExercise>)

    abstract fun nextExercise()

    protected abstract fun initExercise()

    abstract fun startExercise()

    protected abstract fun calculateReps(): Int
}