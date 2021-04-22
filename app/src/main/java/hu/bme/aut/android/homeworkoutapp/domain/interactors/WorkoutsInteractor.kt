package hu.bme.aut.android.homeworkoutapp.domain.interactors

import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.firebase.FirebaseDataSource
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainNewWorkout
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import java.time.LocalDate
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

    suspend fun deleteWorkoutExercise(workoutId: String, exercise: DomainExercise): Result<Unit, Exception> {
        return firebaseDataSource.deleteWorkoutExercise(workoutId, exercise)
    }

    suspend fun updateWorkoutExercise(workoutId: String, exercise: DomainExercise): Result<Unit, Exception> {
        return firebaseDataSource.updateWorkoutExercise(workoutId, exercise)
    }

    suspend fun getPlannedWorkoutsFromDate(selectedDate: LocalDate): Result<List<DomainWorkout>, Exception> {
        return firebaseDataSource.getWorkoutsFromDate(selectedDate)
    }

    suspend fun deletePlannedWorkoutFromDate(selectedDate: LocalDate, workout: DomainWorkout): Result<Unit, Exception> {
        return firebaseDataSource.deletePlannedWorkoutFromDate(selectedDate, workout)
    }

    suspend fun addPlannedWorkoutToDate(selectedDate: LocalDate, workout: DomainWorkout): Result<Unit, Exception> {
        return firebaseDataSource.addPlannedWorkoutToDate(selectedDate, workout)
    }


}