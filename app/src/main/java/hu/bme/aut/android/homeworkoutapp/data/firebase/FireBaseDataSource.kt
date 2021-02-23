package hu.bme.aut.android.homeworkoutapp.data.firebase

import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireBaseDataSource @Inject constructor() {

    suspend fun getWorkouts(): List<DomainWorkout> {
        return listOf()
    }

}