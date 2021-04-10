package hu.bme.aut.android.homeworkoutapp.domain.interactors

import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.firebase.FirebaseDataSource
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainNewWorkout
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

    suspend fun addWorkout(workout: DomainNewWorkout): Result<Unit, Exception> {
        return firebaseDataSource.addWorkout(workout)
    }

    suspend fun deleteWorkout(workout: DomainWorkout): Result<Unit, Exception> {
        return firebaseDataSource.deleteWorkout(workout)
    }

    suspend fun getWorkoutExercises(workoutId: String): Result<List<DomainExercise>, Exception> {
        return firebaseDataSource.getWorkoutExercises(workoutId)
    }

    suspend fun addExerciseToWorkout(exercise: DomainExercise, workoutId: String) :  Result<Unit, Exception> {
        return firebaseDataSource.addExerciseToWorkout(exercise, workoutId)
    }

}