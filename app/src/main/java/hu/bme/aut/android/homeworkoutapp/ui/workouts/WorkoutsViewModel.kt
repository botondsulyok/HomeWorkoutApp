package hu.bme.aut.android.homeworkoutapp.ui.workouts

import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import javax.inject.Inject

class WorkoutsViewModel @Inject constructor(
    private val workoutsPresenter: WorkoutsPresenter
) : WorkoutsViewModelBase() {

    override fun loadWorkouts() = execute {
        getWorkouts()
    }

    override fun deleteWorkout(workout: UiWorkout) = execute {
        viewState = Loading
        when(val result = workoutsPresenter.deleteWorkout(workout)) {
            is ResultSuccess -> {
                getWorkouts()
            }
            is ResultFailure -> {
                viewState = Failed(result.reason.message.toString())
            }
        }
    }

    private suspend fun getWorkouts() {
        viewState = Loading
        val result = workoutsPresenter.getWorkouts()
        viewState = when (result) {
            is ResultSuccess -> {
                Loaded(result.value)
            }
            is ResultFailure -> {
                Failed(result.reason.message.toString())
            }
        }
    }

}