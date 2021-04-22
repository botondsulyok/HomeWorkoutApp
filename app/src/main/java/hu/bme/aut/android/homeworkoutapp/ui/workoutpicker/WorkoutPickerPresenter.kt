package hu.bme.aut.android.homeworkoutapp.ui.workoutpicker

import android.content.Context
import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.domain.interactors.WorkoutsInteractor
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainExercise
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.exercises.toDomainExercise
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.toDomainWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.toUiWorkout
import java.time.LocalDate
import javax.inject.Inject

class WorkoutPickerPresenter @Inject constructor(
    private val workoutsInteractor: WorkoutsInteractor
) {

    suspend fun getWorkouts(): Result<List<UiWorkout>, Exception> = withIOContext {
        when (val result = workoutsInteractor.getWorkouts()) {
            is ResultSuccess -> {
                ResultSuccess(value = result.value.map { it.toUiWorkout() })
            }
            is ResultFailure -> {
                ResultFailure(reason = result.reason)
            }
        }
    }

}