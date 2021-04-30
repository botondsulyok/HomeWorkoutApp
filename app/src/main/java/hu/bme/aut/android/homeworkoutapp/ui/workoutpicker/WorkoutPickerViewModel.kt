package hu.bme.aut.android.homeworkoutapp.ui.workoutpicker

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import java.time.LocalDate
import javax.inject.Inject

class WorkoutPickerViewModel @Inject constructor(
    private val workoutPickerPresenter: WorkoutPickerPresenter
) : WorkoutPickerViewModelBase() {

    override fun getWorkouts() = execute {
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

    override fun toReadyState() {
        viewState = Ready
    }

}