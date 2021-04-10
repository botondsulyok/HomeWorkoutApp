package hu.bme.aut.android.homeworkoutapp.ui.workout

import android.content.Context
import co.zsmb.rainbowcake.withIOContext
import hu.bme.aut.android.homeworkoutapp.data.Result
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.domain.interactors.WorkoutsInteractor
import hu.bme.aut.android.homeworkoutapp.domain.models.DomainWorkout
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.exercises.toUiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import javax.inject.Inject

class WorkoutPresenter @Inject constructor(
    private val workoutsInteractor: WorkoutsInteractor,
    private val context: Context
) {

    suspend fun getWorkoutExercises(workoutId: String): Result<List<UiExercise>, Exception> = withIOContext {
        when(val result = workoutsInteractor.getWorkoutExercises(workoutId)) {
            is ResultSuccess -> {
                ResultSuccess(value = result.value.map { it.toUiExercise(context) })
            }
            is ResultFailure -> {
                ResultFailure(reason = result.reason)
            }
        }
    }

}