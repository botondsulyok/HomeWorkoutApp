package hu.bme.aut.android.homeworkoutapp.data.models

import com.google.firebase.firestore.ServerTimestamp
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import java.util.*

data class FirebaseExercise(
        val id: String = "",
        val name: String = "",
        @ServerTimestamp
        val creation: Date = Date(),
        val reps: Int = 0,
        val duration: Duration = Duration(),
        val categoryValue: String = "",
        val videoPath: String = "",
        val videoUrl: String = ""
)