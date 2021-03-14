package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import android.content.Context
import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.domain.interactors.ExercisesInteractor
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainNewExercise
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import javax.inject.Inject

class NewExercisePresenter @Inject constructor(
    private val exercisesInteractor: ExercisesInteractor,
    private val context: Context
) {

    suspend fun addExercise(exercise: UiNewExercise): Result<Unit, Exception> = withIOContext {
        exercisesInteractor.addExercise(exercise.toDomainNewExercise())
    }

    private fun UiNewExercise.toDomainNewExercise(): DomainNewExercise {
        // TODO categoryValue

        val categoriesEntryList = context.resources.getStringArray(R.array.exercise_categories_entries)
        val categoriesValuesList = context.resources.getStringArray(R.array.exercise_categories_values)

        return DomainNewExercise(
            name = name,
            duration = duration,
            reps = reps,
            categoryValue = categoriesValuesList[categoriesEntryList.indexOf(categoryEntry)],
            videoUri = videoUri
        )
    }

}