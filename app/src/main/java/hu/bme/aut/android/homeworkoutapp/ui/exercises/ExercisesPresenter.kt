package hu.bme.aut.android.homeworkoutapp.ui.exercises

import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.domain.interactors.ExercisesInteractor
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import javax.inject.Inject

class ExercisesPresenter @Inject constructor(
    private val exerciseInteractor: ExercisesInteractor
) {

    suspend fun getExercises(): Result<List<UiExercise>, Exception> = withIOContext {
        when(val result = exerciseInteractor.getExercises()) {
            is ResultSuccess -> {
                ResultSuccess(value = result.value.map { it.toUiExercise() })
            }
            is ResultFailure -> {
                ResultFailure(reason = result.reason)
            }
        }
    }

    suspend fun deletExercise(exercise: UiExercise): Result<Unit, Exception> = withIOContext {
        exerciseInteractor.deleteExercise(exercise.toDomainExercise())
    }

}

private fun UiExercise.toDomainExercise(): DomainExercise {
    return DomainExercise(
        id = id,
        name = name
    )
}

private fun DomainExercise.toUiExercise(): UiExercise {
    return UiExercise(
        id = id,
        name = name
    )
}