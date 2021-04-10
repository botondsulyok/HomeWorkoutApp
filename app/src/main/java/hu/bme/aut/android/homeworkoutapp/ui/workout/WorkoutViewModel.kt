package hu.bme.aut.android.homeworkoutapp.ui.workout

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import javax.inject.Inject

class WorkoutViewModel @Inject constructor(
    private val workoutPresenter: WorkoutPresenter
) : RainbowCakeViewModel<WorkoutViewState>(Loading) {

    fun getWorkoutExercises(workoutId: String) = execute {
        viewState = Loading
        val result = workoutPresenter.getWorkoutExercises(workoutId)
        viewState = when(result) {
            is ResultSuccess -> {
                Loaded(result.value)
            }
            is ResultFailure -> {
                Failed(result.reason.message.toString())
            }
        }
    }

}