package hu.bme.aut.android.homeworkoutapp.ui.workouts

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.welcome.SignInFailed
import hu.bme.aut.android.homeworkoutapp.ui.welcome.SignedIn
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
                LoadingFailed(result.reason.message.toString())
            }
        }

    }

}