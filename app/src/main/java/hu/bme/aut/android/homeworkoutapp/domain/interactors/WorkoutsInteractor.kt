package hu.bme.aut.android.homeworkoutapp.domain.interactors

import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.firebase.FirebaseDataSource
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainNewWorkout
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import java.time.LocalDate
import java.util.*
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

    suspend fun getPlannedWorkoutsFromDate(selectedDate: Date): Result<List<DomainWorkout>, Exception> {
        return firebaseDataSource.getPlannedWorkoutsFromDate(selectedDate)
    }

    suspend fun getPlannedDaysFromMonth(month: Date): Result<List<Date>, Exception> {
        return firebaseDataSource.getPlannedDaysFromMonth(month)
    }

    suspend fun deletePlannedWorkoutFromDate(selectedDate: Date, workout: DomainWorkout): Result<Unit, Exception> {
        return firebaseDataSource.deletePlannedWorkoutFromDate(selectedDate, workout)
    }

    suspend fun addPlannedWorkoutToDate(selectedDate: Date, workout: DomainWorkout): Result<Unit, Exception> {
        return firebaseDataSource.addPlannedWorkoutToDate(selectedDate, workout)
    }


}