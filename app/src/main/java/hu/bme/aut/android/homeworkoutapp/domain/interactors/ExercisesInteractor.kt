package hu.bme.aut.android.homeworkoutapp.domain.interactors

import hu.bme.aut.android.homeworkoutapp.data.firebase.FirebaseDataSource
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import javax.inject.Inject
import javax.inject.Singleton
import hu.bme.aut.android.homeworkoutapp.data.Result

@Singleton
class ExercisesInteractor @Inject constructor(
        private val firebaseDataSource: FirebaseDataSource
) {

    suspend fun getExercises(): Result<List<DomainExercise>, Exception> {
        return firebaseDataSource.getExercises()
    }

    suspend fun addExercise(exercise: DomainExercise): Result<Unit, Exception> {
        return firebaseDataSource.addExercise(exercise)
    }

    suspend fun deleteExercise(exercise: DomainExercise): Result<Unit, Exception> {
        return firebaseDataSource.deleteExercise(exercise)
    }

}