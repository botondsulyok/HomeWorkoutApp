package hu.bme.aut.android.homeworkoutapp.ui.exercises

import android.content.Context
import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.domain.interactors.ExercisesInteractor
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import javax.inject.Inject

class ExercisesPresenter @Inject constructor(
    private val exerciseInteractor: ExercisesInteractor,
    private val context: Context
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

    suspend fun deleteExercise(exercise: UiExercise): Result<Unit, Exception> = withIOContext {
        exerciseInteractor.deleteExercise(exercise.toDomainExercise())
    }

    private fun UiExercise.toDomainExercise(): DomainExercise {
        val categoriesEntryList = context.resources.getStringArray(R.array.exercise_categories_entries)
        val categoriesValuesList = context.resources.getStringArray(R.array.exercise_categories_values)
        val categoryValue =
                if(categoriesEntryList.contains(categoryEntry)) {
                    categoriesValuesList[categoriesEntryList.indexOf(categoryEntry)]
                }
                else {
                    ""
                }
        return DomainExercise(
                id = id,
                name = name,
                reps = reps,
                duration = duration,
                categoryValue = categoryValue,
                videoUrl = videoUrl
        )
    }

    private fun DomainExercise.toUiExercise(): UiExercise {
        val categoriesEntryList = context.resources.getStringArray(R.array.exercise_categories_entries)
        val categoriesValuesList = context.resources.getStringArray(R.array.exercise_categories_values)
        val categoryEntry =
                if(categoriesValuesList.contains(categoryValue)) {
                    categoriesEntryList[categoriesValuesList.indexOf(categoryValue)]
                }
                else {
                    ""
                }
        return UiExercise(
                id = id,
                name = name,
                reps = reps,
                duration = duration,
                categoryEntry = categoryEntry,
                videoUrl = videoUrl
        )
    }
}

