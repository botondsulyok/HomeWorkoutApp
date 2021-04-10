package hu.bme.aut.android.homeworkoutapp.ui.workoutpicker

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import javax.inject.Inject

class WorkoutPickerViewModel @Inject constructor(
    private val workoutPickerPresenter: WorkoutPickerPresenter
) : RainbowCakeViewModel<WorkoutPickerViewState>(Loading) {

    fun getWorkouts() = execute {
        viewState = Loading
        val result = workoutPickerPresenter.getWorkouts()
        viewState = when (result) {
            is ResultSuccess -> {
                Loaded(result.value)
            }
            is ResultFailure -> {
                Failed(result.reason.message.toString())
            }
        }
    }

    fun addExerciseToWorkout(
        exercise: UiExercise,
        workoutId: String
    ) = execute {
        viewState = Uploading
        val result = workoutPickerPresenter.addExerciseToWorkout(exercise, workoutId)
        viewState = when(result) {
            is ResultSuccess -> {
                UploadSuccess
            }
            is ResultFailure -> {
                UploadFailed(result.reason.message.toString())
            }
        }
    }

    fun toReadyState() {
        viewState = Ready
    }

}