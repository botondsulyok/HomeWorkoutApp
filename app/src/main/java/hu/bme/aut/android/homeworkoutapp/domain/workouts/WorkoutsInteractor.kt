package hu.bme.aut.android.homeworkoutapp.domain.workouts

import hu.bme.aut.android.homeworkoutapp.data.firebase.FireBaseDataSource
import hu.bme.aut.android.homeworkoutapp.domain.workouts.models.DomainWorkout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutsInteractor @Inject constructor(
    private val fireBaseDataSource: FireBaseDataSource,
) {

    suspend fun getWorkouts(): List<DomainWorkout> {
        //fireBaseDataSource.getWorkouts()
        return listOf()
    }

}