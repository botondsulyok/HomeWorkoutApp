package hu.bme.aut.android.homeworkoutapp.ui.exercises

import android.util.Log
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import javax.inject.Inject

class ExercisesViewModel @Inject constructor(
    private val exercisesPresenter: ExercisesPresenter
) : ExercisesViewModelBase() {

    override fun loadExercises() = execute {
        getExercises()
    }

    override fun deleteExercise(exercise: UiExercise) = execute {
        viewState = Loading
        when(val result = exercisesPresenter.deleteExercise(exercise)) {
            is ResultSuccess -> {
                getExercises()
            }
            is ResultFailure -> {
                viewState = Failed(result.reason.message.toString())
            }
        }
    }

    override fun updateExercise(exercise: UiExercise) = execute {
        viewState = Loading
        when(val result = exercisesPresenter.updateExercise(exercise)) {
            is ResultSuccess -> {
                getExercises()
            }
            is ResultFailure -> {
                viewState = Failed(result.reason.message.toString())
            }
        }
    }

    override fun addExerciseToWorkout(
        exercise: UiExercise,
        workoutId: String
    ) = execute {

        Log.i("asd", "asd")

        viewState = Uploading
        val result = exercisesPresenter.addExerciseToWorkout(exercise, workoutId)
        viewState = when(result) {
            is ResultSuccess -> {
                UploadSuccess
            }
            is ResultFailure -> {
                Failed(result.reason.message.toString())
            }
        }
    }

    private suspend fun getExercises() {
        viewState = Loading
        val result = exercisesPresenter.getExercises()
        viewState = when (result) {
            is ResultSuccess -> {
                Loaded(result.value)
            }
            is ResultFailure -> {
                Failed(result.reason.message.toString())
            }
        }
    }

}