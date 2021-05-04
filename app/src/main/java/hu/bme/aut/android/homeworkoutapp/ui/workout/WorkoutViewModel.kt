package hu.bme.aut.android.homeworkoutapp.ui.workout

import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import javax.inject.Inject

class WorkoutViewModel @Inject constructor(
    private val workoutPresenter: WorkoutPresenter
) : WorkoutViewModelBase() {

    override var workoutId = ""

    override fun loadWorkoutExercises() = execute {
        getWorkoutExercises()
    }

    override fun deleteWorkoutExercise(exercise: UiExercise) = execute {
        viewState = Loading
        when(val result = workoutPresenter.deleteWorkoutExercise(workoutId, exercise)) {
            is ResultSuccess -> {
                getWorkoutExercises()
            }
            is ResultFailure -> {
                viewState =
                    Failed(result.reason.message.toString())
            }
        }
    }

    override fun updateWorkoutExercise(exercise: UiExercise) = execute {
        viewState = Loading
        when(val result = workoutPresenter.updateWorkoutExercise(workoutId, exercise)) {
            is ResultSuccess -> {
                getWorkoutExercises()
            }
            is ResultFailure -> {
                viewState =
                    Failed(result.reason.message.toString())
            }
        }
    }

    private suspend fun getWorkoutExercises() {
        viewState = Loading
        val result = workoutPresenter.getWorkoutExercises(workoutId)
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