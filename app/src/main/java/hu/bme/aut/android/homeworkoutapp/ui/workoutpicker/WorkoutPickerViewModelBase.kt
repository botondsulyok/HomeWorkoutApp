package hu.bme.aut.android.homeworkoutapp.ui.workoutpicker

import co.zsmb.rainbowcake.base.RainbowCakeViewModel

abstract class WorkoutPickerViewModelBase : RainbowCakeViewModel<WorkoutPickerViewState>(Loading) {
    abstract fun getWorkouts()

    abstract fun toReadyState()
}