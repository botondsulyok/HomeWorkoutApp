package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.domain.interactors.ExercisesInteractor
import hu.bme.aut.android.homeworkoutapp.domain.interactors.WorkoutsInteractor
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainNewExercise
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout
import javax.inject.Inject

class NewExercisePresenter @Inject constructor(
    private val exercisesInteractor: ExercisesInteractor
) {

    suspend fun addExercise(exercise: UiNewExercise): Result<Unit, Exception> = withIOContext {
        exercisesInteractor.addExercise(exercise.toDomainNewExercise())
    }

}

private fun UiNewExercise.toDomainNewExercise(): DomainNewExercise {
    // TODO categoryValue
    return DomainNewExercise(
        name = name,
        duration = duration,
        reps = reps,
        categoryValue = categoryEntry,
        videoUri = videoUri
    )
}