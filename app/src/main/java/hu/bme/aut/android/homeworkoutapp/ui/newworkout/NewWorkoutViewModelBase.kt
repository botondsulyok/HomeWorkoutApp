package hu.bme.aut.android.homeworkoutapp.ui.newworkout

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout

abstract class NewWorkoutViewModelBase : RainbowCakeViewModel<NewWorkoutViewState>(Initial) {
    abstract fun addWorkout(workout: UiNewWorkout)

    abstract fun toInitialState()
}