package hu.bme.aut.android.homeworkoutapp.data.models

data class FirebasePlannedWorkoutDay(
    val workouts: Map<String, FirebaseWorkout> = mapOf()
)
