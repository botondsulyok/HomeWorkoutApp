package hu.bme.aut.android.homeworkoutapp.core.testviewmodels

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.homeworkoutapp.ui.plans.PlannedWorkoutsViewModelBase
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class TestPlannedWorkoutsViewModel @Inject constructor() : PlannedWorkoutsViewModelBase() {

    override var selectedDate: LocalDate = LocalDate.now()

    override val plannedWorkoutsFromMonthLiveData: MutableLiveData<List<Date>> = MutableLiveData()

    override fun getPlannedWorkoutsFromDate() {

    }

    override fun getPlannedDaysFromMonth(month: LocalDate) {

    }

    override fun addPlannedWorkoutToDate(workout: UiWorkout) {

    }

    override fun deletePlannedWorkoutFromDate(workout: UiWorkout) {

    }

}