package hu.bme.aut.android.homeworkoutapp.data.firebase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.data.models.FirebaseExercise
import hu.bme.aut.android.homeworkoutapp.data.models.FirebaseWorkout
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout
import kotlinx.coroutines.tasks.await
import java.util.concurrent.ExecutionException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor() {

    private val auth = Firebase.auth

    private val db = Firebase.firestore

    private var userId = auth.currentUser?.uid.toString()

    suspend fun signInWithGoogle(
        credential: AuthCredential
    ): Result<Unit, Exception> {
        return try {
            auth.signInWithCredential(credential).await()
            userId = auth.currentUser?.uid.toString()
            ResultSuccess(Unit)
        } catch (e: Exception) {
            ResultFailure(e)
        }
    }

    suspend fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun signOut() {
        auth.signOut()
        userId = auth.currentUser?.uid.toString()
    }

    suspend fun getWorkouts(): Result<List<DomainWorkout>, Exception> {
        val workoutsRef = db
            .collection("userdata")
            .document(userId)
            .collection("workouts")
            .orderBy("creation", Query.Direction.DESCENDING)

        return try {
            val workoutsSnapshot = workoutsRef.get().await()
            val workouts = workoutsSnapshot.documents.map {
                it.toObject<FirebaseWorkout>()?.toDomainWorkout() ?: DomainWorkout()
            }
            ResultSuccess(workouts)
        } catch (e: Exception) {
            ResultFailure(e)
        }

    }

    suspend fun addWorkout(workout: DomainWorkout): Result<Unit, Exception> {
        val newWorkoutRef = db
            .collection("userdata")
            .document(userId)
            .collection("workouts")
            .document()

        val newWorkout = workout.toFirebaseWorkout(newWorkoutRef.id)

        return try {
            newWorkoutRef.set(newWorkout).await()
            ResultSuccess(Unit)
        } catch (e: Exception) {
            ResultFailure(e)
        }

    }

    suspend fun deleteWorkout(workout: DomainWorkout): Result<Unit, Exception> {
        val deleteWorkout = workout.toFirebaseWorkout()

        val deleteWorkoutRef = db
            .collection("userdata")
            .document(userId)
            .collection("workouts")
            .document(deleteWorkout.id)

        return try {
            deleteWorkoutRef.delete().await()
            ResultSuccess(Unit)
        } catch (e: Exception) {
            ResultFailure(e)
        }
    }

    suspend fun getExercises(): Result<List<DomainExercise>, Exception> {
        val exercisesRef = db
                .collection("userdata")
                .document(userId)
                .collection("exercises")
                .orderBy("creation", Query.Direction.DESCENDING)

        return try {
            val workoutsSnapshot = exercisesRef.get().await()
            val workouts = workoutsSnapshot.documents.map {
                it.toObject<FirebaseExercise>()?.toDomainExercise() ?: DomainExercise()
            }
            ResultSuccess(workouts)
        } catch (e: Exception) {
            ResultFailure(e)
        }

    }

    suspend fun addExercise(exercise: DomainExercise): Result<Unit, Exception> {
        val newExerciseRef = db
                .collection("userdata")
                .document(userId)
                .collection("exercises")
                .document()

        val newExercise = exercise.toFirebaseExercise(newExerciseRef.id)

        return try {
            newExerciseRef.set(newExercise).await()
            ResultSuccess(Unit)
        } catch (e: Exception) {
            ResultFailure(e)
        }

    }

    suspend fun deleteExercise(exercise: DomainExercise): Result<Unit, Exception> {
        val deleteExercise = exercise.toFirebaseExercise()

        val deleteExerciseRef = db
                .collection("userdata")
                .document(userId)
                .collection("exercises")
                .document(deleteExercise.id)

        return try {
            deleteExerciseRef.delete().await()
            ResultSuccess(Unit)
        } catch (e: Exception) {
            ResultFailure(e)
        }
    }


}

private fun DomainWorkout.toFirebaseWorkout(id: String = this.id): FirebaseWorkout {
    return FirebaseWorkout(
        id = id,
        name = name
    )
}

private fun FirebaseWorkout.toDomainWorkout(): DomainWorkout {
    return DomainWorkout(
        id = id,
        name = name
    )
}

private fun DomainExercise.toFirebaseExercise(id: String = this.id): FirebaseExercise {
    return FirebaseExercise(
            id = id,
            name = name
    )
}

private fun FirebaseExercise.toDomainExercise(): DomainExercise {
    return DomainExercise(
            id = id,
            name = name
    )
}