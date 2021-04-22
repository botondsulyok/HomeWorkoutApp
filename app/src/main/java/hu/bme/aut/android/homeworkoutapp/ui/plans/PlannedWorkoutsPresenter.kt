package hu.bme.aut.android.homeworkoutapp.ui.plans

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.domain.interactors.WorkoutsInteractor
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.toDomainWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.toUiWorkout
import hu.bme.aut.android.homeworkoutapp.utils.toDate
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

class PlannedWorkoutsPresenter @Inject constructor(
    private val workoutsInteractor: WorkoutsInteractor
) {

    suspend fun getPlannedWorkoutsFromDate(selectedDate: LocalDate): Result<List<UiWorkout>, Exception> = withIOContext {
        when(val result = workoutsInteractor.getPlannedWorkoutsFromDate(selectedDate.toDate())) {
            is ResultSuccess -> {
                ResultSuccess(value = result.value.map { it.toUiWorkout() })
            }
            is ResultFailure -> {
                ResultFailure(reason = result.reason)
            }
        }
    }

    suspend fun deletePlannedWorkoutFromDate(selectedDate: LocalDate, workout: UiWorkout): Result<Unit, Exception> = withIOContext {
        workoutsInteractor.deletePlannedWorkoutFromDate(selectedDate.toDate(), workout.toDomainWorkout())
    }

    suspend fun addPlannedWorkoutToDate(selectedDate: LocalDate, workout: UiWorkout): Result<Unit, Exception> = withIOContext {
        when (val result = workoutsInteractor.addPlannedWorkoutToDate(selectedDate.toDate(), workout.toDomainWorkout())) {
            is ResultSuccess -> {
                ResultSuccess(Unit)
            }
            is ResultFailure -> {
                ResultFailure(reason = result.reason)
            }
        }
    }

}