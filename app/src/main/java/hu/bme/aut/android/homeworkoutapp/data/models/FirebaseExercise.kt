package hu.bme.aut.android.homeworkoutapp.data.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class FirebaseExercise(
        val id: String = "",
        val name: String = "",
        @ServerTimestamp
        val creation: Date = Date()
)