package hu.bme.aut.android.homeworkoutapp.ui.workouts

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout

abstract class WorkoutsViewModelBase
    : RainbowCakeViewModel<WorkoutsViewState>(Loading) {

    abstract fun loadWorkouts()

    abstract fun deleteWorkout(workout: UiWorkout)

}