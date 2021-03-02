package hu.bme.aut.android.homeworkoutapp.domain.interactors

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.data.firebase.FireBaseDataSource
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutsInteractor @Inject constructor(
    private val fireBaseDataSource: FireBaseDataSource
    ) {

    suspend fun getWorkouts(): List<DomainWorkout> {
        //fireBaseDataSource.getWorkouts()
        return listOf()
    }

    suspend fun addWorkout(workout: DomainWorkout) = withIOContext {
        fireBaseDataSource.addWorkout(workout)
    }

}