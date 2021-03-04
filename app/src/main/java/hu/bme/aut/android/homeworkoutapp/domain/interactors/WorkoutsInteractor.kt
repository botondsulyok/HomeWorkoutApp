package hu.bme.aut.android.homeworkoutapp.domain.interactors

import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.firebase.FirebaseDataSource
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutsInteractor @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
    ) {

    suspend fun getWorkouts(): Result<List<DomainWorkout>, Exception> {
        return firebaseDataSource.getWorkouts()
    }

    suspend fun addWorkout(workout: DomainWorkout): Result<Unit, Exception> {
        return firebaseDataSource.addWorkout(workout)
    }

    suspend fun deleteWorkout(workout: DomainWorkout): Result<Unit, Exception> {
        return firebaseDataSource.deleteWorkout(workout)
    }

}