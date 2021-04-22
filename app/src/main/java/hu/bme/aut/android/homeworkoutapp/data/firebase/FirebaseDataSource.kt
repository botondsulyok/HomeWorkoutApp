package hu.bme.aut.android.homeworkoutapp.data.firebase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.data.models.FirebaseExercise
import hu.bme.aut.android.homeworkoutapp.data.models.FirebaseWorkout
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainNewExercise
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainNewWorkout
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import hu.bme.aut.android.homeworkoutapp.utils.monthYearFormatter
import hu.bme.aut.android.homeworkoutapp.utils.yearFormatter
import kotlinx.coroutines.tasks.await
import java.net.URLEncoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor() {

    private val auth = Firebase.auth

    private val db = Firebase.firestore

    private val storage = Firebase.storage.reference

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

    suspend fun addWorkout(workout: DomainNewWorkout): Result<Unit, Exception> {
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

    suspend fun addPlannedWorkoutToDate(selectedDate: LocalDate, workout: DomainWorkout): Result<Unit, Exception> {
        val newWorkoutRef = db
            .collection("userdata")
            .document(userId)
            .collection("plannedworkouts")
            .document(monthYearFormatter.format(selectedDate))
            .collection(yearFormatter.format(selectedDate))
            .document(workout.id)

        val newWorkout = workout.toFirebaseWorkout()

        return try {
            newWorkoutRef.set(newWorkout).await()
            ResultSuccess(Unit)
        } catch (e: Exception) {
            ResultFailure(e)
        }

    }

    suspend fun getWorkoutsFromDate(selectedDate: LocalDate): Result<List<DomainWorkout>, Exception> {
        val workoutsRef = db
            .collection("userdata")
            .document(userId)
            .collection("plannedworkouts")
            .document(monthYearFormatter.format(selectedDate))
            .collection(yearFormatter.format(selectedDate))
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

    suspend fun deletePlannedWorkoutFromDate(selectedDate: LocalDate, workout: DomainWorkout): Result<Unit, Exception> {
        val deleteWorkoutRef = db
            .collection("userdata")
            .document(userId)
            .collection("plannedworkouts")
            .document(monthYearFormatter.format(selectedDate))
            .collection(yearFormatter.format(selectedDate))
            .document(workout.id)

        return try {
            deleteWorkoutRef.delete().await()
            ResultSuccess(Unit)
        } catch (e: Exception) {
            ResultFailure(e)
        }
    }

    suspend fun deleteWorkout(workout: DomainWorkout): Result<Unit, Exception> {
        val deleteWorkoutRef = db
            .collection("userdata")
            .document(userId)
            .collection("workouts")
            .document(workout.id)

        return try {
            deleteWorkoutRef.delete().await()
            ResultSuccess(Unit)
        } catch (e: Exception) {
            ResultFailure(e)
        }
    }

    suspend fun getWorkoutExercises(workoutId: String): Result<List<DomainExercise>, Exception> {
        // TODO nem jó még
        val exercisesRef = db
            .collection("userdata")
            .document(userId)
            .collection("workouts")
            .document(workoutId)
            .collection("exercises")
            .orderBy("creation", Query.Direction.DESCENDING)

        return try {
            val exercisesSnapshot = exercisesRef.get().await()
            val workouts = exercisesSnapshot.documents.map {
                it.toObject<FirebaseExercise>()?.toDomainExercise() ?: DomainExercise()
            }
            ResultSuccess(workouts)
        } catch (e: Exception) {
            ResultFailure(e)
        }

    }

    suspend fun addExerciseToWorkout(exercise: DomainExercise, workoutId: String) :  Result<Unit, Exception> {
        val newExerciseRef = db
            .collection("userdata")
            .document(userId)
            .collection("workouts")
            .document(workoutId)
            .collection("exercises")
            .document(exercise.id)

        return try {
            val newExercise = exercise.toFirebaseExercise()
            newExerciseRef.set(newExercise).await()
            ResultSuccess(Unit)
        }
        catch (e: Exception) {
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
            val exercisesSnapshot = exercisesRef.get().await()
            val workouts = exercisesSnapshot.documents.map {
                it.toObject<FirebaseExercise>()?.toDomainExercise() ?: DomainExercise()
            }
            ResultSuccess(workouts)
        } catch (e: Exception) {
            ResultFailure(e)
        }

    }

    suspend fun addExercise(exercise: DomainNewExercise): Result<Unit, Exception> {
        return try {
            var videoPath = ""
            var videoUrl = ""
            if(exercise.videoUri != null) {
                val newVideoName = "${URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8")}.mp4"
                videoPath = "userdata/${userId}/exercises/videos/$newVideoName"
                val newVideoRef = storage.child(videoPath)
                newVideoRef.putFile(exercise.videoUri).await()
                videoUrl = newVideoRef.downloadUrl.await().toString()
            }

            var thumbnailUrl = ""
            if(exercise.thumbnailInBytes != null) {
                val newThumbnailName = "${URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8")}.jpg"
                val thumbnailPath = "userdata/${userId}/exercises/videothumbnails/$newThumbnailName"
                val newThumbnailRef = storage.child(thumbnailPath)
                newThumbnailRef.putBytes(exercise.thumbnailInBytes).await()
                thumbnailUrl = newThumbnailRef.downloadUrl.await().toString()
            }

            val newExerciseRef = db
                .collection("userdata")
                .document(userId)
                .collection("exercises")
                .document()

            val newExercise = exercise.toFirebaseExercise(newExerciseRef.id, videoPath, videoUrl, thumbnailUrl)

            newExerciseRef.set(newExercise).await()
            ResultSuccess(Unit)
        }
        catch (e: Exception) {
            ResultFailure(e)
        }
    }

    // TODO delete video and thumbnail
    suspend fun deleteExercise(exercise: DomainExercise): Result<Unit, Exception> {
        val deleteExerciseRef = db
                .collection("userdata")
                .document(userId)
                .collection("exercises")
                .document(exercise.id)

        return try {
            deleteExerciseRef.delete().await()
            ResultSuccess(Unit)
        } catch (e: Exception) {
            ResultFailure(e)
        }
    }

    suspend fun updateExercise(exercise: DomainExercise): Result<Unit, Exception> {
        val updateExerciseRef = db
            .collection("userdata")
            .document(userId)
            .collection("exercises")
            .document(exercise.id)

        return try {
            val oldExerciseSnapshot = updateExerciseRef.get().await()
            val oldExercise = oldExerciseSnapshot.toObject<FirebaseExercise>()
            if(oldExercise != null) {
                val newExercise = exercise.toFirebaseExercise(oldExercise)
                updateExerciseRef.set(newExercise).await()
            }
            ResultSuccess(Unit)
        } catch (e: Exception) {
            ResultFailure(e)
        }
    }

    suspend fun deleteWorkoutExercise(workoutId: String, exercise: DomainExercise): Result<Unit, Exception> {
        val deleteExerciseRef = db
            .collection("userdata")
            .document(userId)
            .collection("workouts")
            .document(workoutId)
            .collection("exercises")
            .document(exercise.id)

        return try {
            deleteExerciseRef.delete().await()
            ResultSuccess(Unit)
        } catch (e: Exception) {
            ResultFailure(e)
        }
    }

    suspend fun updateWorkoutExercise(workoutId: String, exercise: DomainExercise): Result<Unit, Exception> {
        val updateExerciseRef = db
            .collection("userdata")
            .document(userId)
            .collection("workouts")
            .document(workoutId)
            .collection("exercises")
            .document(exercise.id)

        return try {
            val oldExerciseSnapshot = updateExerciseRef.get().await()
            val oldExercise = oldExerciseSnapshot.toObject<FirebaseExercise>()
            if(oldExercise != null) {
                val newExercise = exercise.toFirebaseExercise(oldExercise)
                updateExerciseRef.set(newExercise).await()
            }
            ResultSuccess(Unit)
        } catch (e: Exception) {
            ResultFailure(e)
        }
    }


}

private fun DomainWorkout.toFirebaseWorkout(): FirebaseWorkout {
    return FirebaseWorkout(
        id = id,
        name = name
    )
}

private fun DomainNewWorkout.toFirebaseWorkout(id: String): FirebaseWorkout {
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

private fun DomainExercise.toFirebaseExercise(firebaseExercise: FirebaseExercise = FirebaseExercise()): FirebaseExercise {
    return FirebaseExercise(
        id = id,
        name = name,
        creation = firebaseExercise.creation,
        reps = reps,
        duration = duration,
        categoryValue = categoryValue,
        videoPath = firebaseExercise.videoPath,
        videoUrl = videoUrl,
        videoLengthInMilliseconds = videoLengthInMilliseconds,
        thumbnailUrl = thumbnailUrl
    )
}

private fun DomainNewExercise.toFirebaseExercise(id: String, videoPath: String, videoUrl:String, thumbnailUrl: String): FirebaseExercise {
    return FirebaseExercise(
        id = id,
        name = name,
        reps = reps,
        duration = duration,
        categoryValue = categoryValue,
        videoPath = videoPath,
        videoUrl = videoUrl,
        videoLengthInMilliseconds = videoLengthInMilliseconds,
        thumbnailUrl = thumbnailUrl
    )
}

private fun FirebaseExercise.toDomainExercise(): DomainExercise {
    return DomainExercise(
        id = id,
        name = name,
        reps = reps,
        duration = duration,
        categoryValue = categoryValue,
        videoUrl = videoUrl,
        videoLengthInMilliseconds = videoLengthInMilliseconds,
        thumbnailUrl = thumbnailUrl
    )
}