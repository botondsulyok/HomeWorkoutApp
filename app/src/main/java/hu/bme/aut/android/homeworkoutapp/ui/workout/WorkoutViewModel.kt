package hu.bme.aut.android.homeworkoutapp.ui.workout

import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.ActionFailed
import hu.bme.aut.android.homeworkoutapp.ui.ActionSuccess
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.utils.ResourcesHelper
import javax.inject.Inject

class WorkoutViewModel @Inject constructor(
    private val workoutPresenter: WorkoutPresenter,
    private val resourcesHelper: ResourcesHelper
) : WorkoutViewModelBase() {

    override var workoutId = ""

    override fun loadWorkoutExercises() = execute {
        getWorkoutExercises()
    }

    override fun deleteWorkoutExercise(exercise: UiExercise) = execute {
        viewState = Loading
        when(val result = workoutPresenter.deleteWorkoutExercise(workoutId, exercise)) {
            is ResultSuccess -> {
                postEvent(ActionSuccess(resourcesHelper.getString(R.string.txt_deleted)))
            }
            is ResultFailure -> {
                postEvent(ActionFailed(result.reason.message.toString()))
            }
        }
        getWorkoutExercises()
    }

    override fun updateWorkoutExercise(exercise: UiExercise) = execute {
        viewState = Loading
        when(val result = workoutPresenter.updateWorkoutExercise(workoutId, exercise)) {
            is ResultSuccess -> {
                postEvent(ActionSuccess(resourcesHelper.getString(R.string.txt_updated)))
            }
            is ResultFailure -> {
                postEvent(ActionFailed(result.reason.message.toString()))
            }
        }
        getWorkoutExercises()
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