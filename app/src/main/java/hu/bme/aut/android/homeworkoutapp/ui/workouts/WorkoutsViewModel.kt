package hu.bme.aut.android.homeworkoutapp.ui.workouts

import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.ActionFailed
import hu.bme.aut.android.homeworkoutapp.ui.ActionSuccess
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import hu.bme.aut.android.homeworkoutapp.utils.ResourcesHelper
import javax.inject.Inject

class WorkoutsViewModel @Inject constructor(
    private val workoutsPresenter: WorkoutsPresenter,
    private val resourcesHelper: ResourcesHelper
) : WorkoutsViewModelBase() {

    override fun loadWorkouts() = execute {
        getWorkouts()
    }

    override fun deleteWorkout(workout: UiWorkout) = execute {
        viewState = Loading
        when(val result = workoutsPresenter.deleteWorkout(workout)) {
            is ResultSuccess -> {
                postEvent(ActionSuccess(resourcesHelper.getString(R.string.txt_deleted)))
            }
            is ResultFailure -> {
                postEvent(ActionFailed(result.reason.message.toString()))
            }
        }
        getWorkouts()
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