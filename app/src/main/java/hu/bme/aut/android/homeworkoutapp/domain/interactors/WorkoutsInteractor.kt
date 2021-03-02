package hu.bme.aut.android.homeworkoutapp.domain.interactors

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.firebase.FireBaseDataSource
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutsInteractor @Inject constructor(
    private val fireBaseDataSource: FireBaseDataSource
    ) {

    suspend fun getWorkouts(): Result<List<DomainWorkout>, Exception> {
        return fireBaseDataSource.getWorkouts()
    }

    suspend fun addWorkout(workout: DomainWorkout): Result<Unit, Exception> {
        return fireBaseDataSource.addWorkout(workout)
    }

    suspend fun deleteWorkout(workout: DomainWorkout): Result<Unit, Exception> {
        return fireBaseDataSource.deleteWorkout(workout)
    }

}