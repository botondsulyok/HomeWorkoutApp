package hu.bme.aut.android.homeworkoutapp.ui.newworkout

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import javax.inject.Inject
	
	class NewWorkoutViewModel @Inject constructor(
    private val newWorkoutPresenter: NewWorkoutPresenter
) : RainbowCakeViewModel<NewWorkoutViewState>(Ready) {

    fun addWorkout(workout: UiNewWorkout) = execute {
        newWorkoutPresenter.addWorkout(workout)
    }

}