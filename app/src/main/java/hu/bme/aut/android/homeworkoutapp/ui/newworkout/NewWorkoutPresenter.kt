package hu.bme.aut.android.homeworkoutapp.ui.newworkout

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.domain.interactors.WorkoutsInteractor
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainNewWorkout
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import javax.inject.Inject

class NewWorkoutPresenter @Inject constructor(
	private val workoutsInteractor: WorkoutsInteractor) {

    suspend fun addWorkout(workout: UiNewWorkout): Result<Unit, Exception> = withIOContext {
        workoutsInteractor.addWorkout(workout.toDomainNewWorkout())
    }

}

private fun UiNewWorkout.toDomainNewWorkout(): DomainNewWorkout {
    return DomainNewWorkout(
        name = name
    )
}