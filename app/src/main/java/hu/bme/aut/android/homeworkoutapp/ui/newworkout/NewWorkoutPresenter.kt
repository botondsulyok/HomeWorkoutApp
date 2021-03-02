package hu.bme.aut.android.homeworkoutapp.ui.newworkout

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.domain.interactors.WorkoutsInteractor
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import javax.inject.Inject

class NewWorkoutPresenter @Inject constructor(
	private val workoutsInteractor: WorkoutsInteractor) {

    suspend fun addWorkout(workout: UiNewWorkout) = withIOContext {
        workoutsInteractor.addWorkout(workout.toDomainWorkout())
    }

}

private fun UiNewWorkout.toDomainWorkout(): DomainWorkout {
    return DomainWorkout(
        name = name
    )
}