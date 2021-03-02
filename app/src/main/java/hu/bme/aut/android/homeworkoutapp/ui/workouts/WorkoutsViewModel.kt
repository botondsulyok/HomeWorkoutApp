package hu.bme.aut.android.homeworkoutapp.ui.workouts

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import javax.inject.Inject

class WorkoutsViewModel @Inject constructor(
    private val workoutPresenter: WorkoutPresenter
) : RainbowCakeViewModel<WorkoutsViewState>(Loading) {

    fun getWorkouts() = execute {
        viewState = Loading
        val result = workoutPresenter.getWorkouts()
        viewState = when(result) {
            is ResultSuccess -> {
                Loaded(result.value)
            }
            is ResultFailure -> {
                Failed(result.reason.message.toString())
            }
        }
    }

    fun deleteWorkout(workout: UiWorkout) = execute {
        viewState = Loading
        when(val result = workoutPresenter.deleteWorkout(workout)) {
            is ResultSuccess -> {
                getWorkouts()
            }
            is ResultFailure -> {
                viewState = Failed(result.reason.message.toString())
            }
        }
    }

}