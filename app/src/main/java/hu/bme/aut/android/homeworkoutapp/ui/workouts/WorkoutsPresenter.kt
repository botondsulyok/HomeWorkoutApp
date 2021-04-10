package hu.bme.aut.android.homeworkoutapp.ui.workouts

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.domain.interactors.WorkoutsInteractor
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import javax.inject.Inject

class WorkoutsPresenter @Inject constructor(
    private val workoutsInteractor: WorkoutsInteractor
) {

    suspend fun getWorkouts(): Result<List<UiWorkout>, Exception> = withIOContext {
        when(val result = workoutsInteractor.getWorkouts()) {
            is ResultSuccess -> {
                ResultSuccess(value = result.value.map { it.toUiWorkout() })
            }
            is ResultFailure -> {
                ResultFailure(reason = result.reason)
            }
        }
    }

    suspend fun deleteWorkout(workout: UiWorkout): Result<Unit, Exception> = withIOContext {
        workoutsInteractor.deleteWorkout(workout.toDomainWorkout())
    }

}

private fun UiWorkout.toDomainWorkout(): DomainWorkout {
    return DomainWorkout(
        id = id,
        name = name
    )
}

private fun DomainWorkout.toUiWorkout(): UiWorkout {
    return UiWorkout(
        id = id,
        name = name
    )
}