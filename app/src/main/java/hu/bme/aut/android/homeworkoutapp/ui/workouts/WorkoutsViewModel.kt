package hu.bme.aut.android.homeworkoutapp.ui.workouts

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class WorkoutsViewModel @Inject constructor(
    private val workoutPresenter: WorkoutPresenter
) : RainbowCakeViewModel<WorkoutsViewState>(WorkoutsLoading) {

    fun loadWorkouts() = execute {
        viewState = WorkoutsLoading
        viewState = WorkoutsReady(workoutPresenter.getWorkouts())
    }

}