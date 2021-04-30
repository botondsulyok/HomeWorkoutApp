package hu.bme.aut.android.homeworkoutapp.ui.plans

import androidx.lifecycle.MutableLiveData
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import java.time.LocalDate
import java.util.*

abstract class PlannedWorkoutsViewModelBase :
    RainbowCakeViewModel<PlannedWorkoutsViewState>(PlannedWorkoutsLoading) {
    abstract var selectedDate: LocalDate
    abstract val plannedWorkoutsFromMonthLiveData: MutableLiveData<List<Date>>

    abstract fun getPlannedWorkoutsFromDate()

    abstract fun getPlannedDaysFromMonth(month: LocalDate)

    abstract fun addPlannedWorkoutToDate(
        workout: UiWorkout
    )

    abstract fun deletePlannedWorkoutFromDate(workout: UiWorkout)
}