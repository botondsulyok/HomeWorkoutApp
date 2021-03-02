package hu.bme.aut.android.homeworkoutapp.ui.workouts

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.domain.interactors.WorkoutsInteractor
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import javax.inject.Inject

class WorkoutPresenter @Inject constructor(
    private val workoutsInteractor: WorkoutsInteractor
) {

    suspend fun getWorkouts(): List<UiWorkout> = withIOContext {
        //workoutsInteractor.getWorkouts()

        //delay(3000)

        listOf()
    }

}