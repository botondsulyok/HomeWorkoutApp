package hu.bme.aut.android.homeworkoutapp.ui.plans

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.ActionFailed
import hu.bme.aut.android.homeworkoutapp.ui.ActionSuccess
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import hu.bme.aut.android.homeworkoutapp.utils.ResourcesHelper
import hu.bme.aut.android.homeworkoutapp.utils.dayMonthYearFormatter
import hu.bme.aut.android.homeworkoutapp.utils.toDate
import hu.bme.aut.android.homeworkoutapp.utils.toMonthStr
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class PlannedWorkoutsViewModel @Inject constructor(
    private val plannedWorkoutsPresenter: PlannedWorkoutsPresenter,
    private val resourcesHelper: ResourcesHelper
) : PlannedWorkoutsViewModelBase() {

    override var selectedDate: LocalDate = LocalDate.now()

    override val plannedWorkoutsFromMonthLiveData: MutableLiveData<List<Date>> by lazy {
        MutableLiveData<List<Date>>()
    }

    override fun getPlannedWorkoutsFromDate() = execute {
        viewState = PlannedWorkoutsLoading
        val result = plannedWorkoutsPresenter.getPlannedWorkoutsFromDate(selectedDate)
        viewState = when(result) {
            is ResultSuccess -> {
                getPlannedDaysFromMonth(selectedDate)
                PlannedWorkoutsLoaded(result.value)
            }
            is ResultFailure -> {
                PlannedWorkoutsFailed(result.reason.message.toString())
            }
        }
    }

    override fun getPlannedDaysFromMonth(month: LocalDate) = execute {
        when(val result = plannedWorkoutsPresenter.getPlannedDaysFromMonth(month)) {
            is ResultSuccess -> {
                plannedWorkoutsFromMonthLiveData.postValue(result.value)
            }
            is ResultFailure -> {
                postEvent(ActionFailed(result.reason.message.toString()))
            }
        }
    }

    override fun addPlannedWorkoutToDate(
        workout: UiWorkout
    ) = execute {
        viewState = PlannedWorkoutsLoading
        when(val result = plannedWorkoutsPresenter.addPlannedWorkoutToDate(selectedDate, workout)) {
            is ResultSuccess -> {
                postEvent(ActionSuccess(resourcesHelper.getString(R.string.txt_added)))
            }
            is ResultFailure -> {
                postEvent(ActionFailed(result.reason.message.toString()))
            }
        }
        getPlannedWorkoutsFromDate()
    }

    override fun deletePlannedWorkoutFromDate(workout: UiWorkout) = execute {
        viewState = PlannedWorkoutsLoading
        when(val result = plannedWorkoutsPresenter.deletePlannedWorkoutFromDate(selectedDate, workout)) {
            is ResultSuccess -> {
                postEvent(ActionSuccess(resourcesHelper.getString(R.string.txt_deleted)))
            }
            is ResultFailure -> {
                postEvent(ActionFailed(result.reason.message.toString()))
            }
        }
        getPlannedWorkoutsFromDate()
    }


}