package hu.bme.aut.android.homeworkoutapp.data.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import kotlinx.coroutines.tasks.await
import java.util.concurrent.ExecutionException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireBaseDataSource @Inject constructor() {

    private val auth = Firebase.auth

    private val db = Firebase.firestore

    suspend fun signInWithGoogle(
        credential: AuthCredential,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure("Authentication failed")
            }
    }

    suspend fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun signOut() {
        auth.signOut()
    }

    suspend fun addWorkout(workout: DomainWorkout): DomainWorkout? {
        val userId = auth.currentUser?.uid.toString()

        val newWorkoutRef = db
            .collection("userdata")
            .document(userId)
            .collection("workouts")
            .document()

        val newWorkout = workout.copy(id = newWorkoutRef.id)

        return try {
            newWorkoutRef
                .set(newWorkout)
                .await()
            newWorkout
        } catch (e: ExecutionException) {
            null
        } catch (e: InterruptedException) {
            null
        }

    }

    /*fun addWorkout(workout: DomainWorkout): Task<Void> {
        val userId = auth.currentUser?.uid.toString()

        val newWorkoutRef = db
            .collection("userdata")
            .document(userId)
            .collection("workouts")
            .document()

        val newWorkout = workout.copy(id = newWorkoutRef.id)

        return newWorkoutRef
                .set(newWorkout)

        /*
        // in the viewmodel
        addWorkout(workout)
            .addOnSuccessListener {
                //move to the next screen
            }
            .addOnFailureListener {
                //show error
            }
         */
    }*/

    suspend fun getWorkouts(): List<DomainWorkout> {
        return listOf()
    }

}