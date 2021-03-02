package hu.bme.aut.android.homeworkoutapp.data.firebase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.concurrent.ExecutionException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireBaseDataSource @Inject constructor() {

    private val auth = Firebase.auth

    private val db = Firebase.firestore

    suspend fun signInWithGoogle(
        credential: AuthCredential
    ): Result<Unit, Exception> {
        return try {
            auth.signInWithCredential(credential).await()
            ResultSuccess(Unit)
        }
        catch (e: ExecutionException) {
            ResultFailure(e)
        }
        catch (e: InterruptedException) {
            ResultFailure(e)
        }
    }

    suspend fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun signOut() {
        auth.signOut()
    }

    suspend fun addWorkout(workout: DomainWorkout): Result<Unit, Exception> {
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
            ResultSuccess(Unit)
        } catch (e: ExecutionException) {
            ResultFailure(e)
        } catch (e: InterruptedException) {
            ResultFailure(e)
        }

    }

    suspend fun getWorkouts(): List<DomainWorkout> {
        return listOf()
    }

}