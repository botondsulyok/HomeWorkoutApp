package hu.bme.aut.android.homeworkoutapp.ui.plans

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.workouts.Failed
import hu.bme.aut.android.homeworkoutapp.ui.workouts.Loaded
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import java.time.LocalDate

class PlannedWorkoutsViewModel(
    private val plannedWorkoutsPresenter: PlannedWorkoutsPresenter
) : RainbowCakeViewModel<PlannedWorkoutsViewState>(PlannedWorkoutsLoading) {

    var selectedDate: LocalDate = LocalDate.now()

    fun getPlannedWorkoutsFromDate() = execute {
        viewState = PlannedWorkoutsLoading
        val result = plannedWorkoutsPresenter.getPlannedWorkoutsFromDate(selectedDate)
        viewState = when(result) {
            is ResultSuccess -> {
                PlannedWorkoutsLoaded(result.value)
            }
            is ResultFailure -> {
                PlannedWorkoutsFailed(result.reason.message.toString())
            }
        }
    }

    fun deletePlannedWorkoutFromDate(workout: UiWorkout) = execute {
        viewState = PlannedWorkoutsLoading
        when(val result = plannedWorkoutsPresenter.deletePlannedWorkoutFromDate(selectedDate, workout)) {
            is ResultSuccess -> {
                getPlannedWorkoutsFromDate()
            }
            is ResultFailure -> {
                viewState = PlannedWorkoutsFailed(result.reason.message.toString())
            }
        }
    }


}