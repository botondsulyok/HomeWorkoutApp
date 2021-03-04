package hu.bme.aut.android.homeworkoutapp.ui.exercises

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import javax.inject.Inject

class ExercisesViewModel @Inject constructor(
    private val exercisesPresenter: ExercisesPresenter
) : RainbowCakeViewModel<ExercisesViewState>(Loading) {

    fun getExercises() = execute {
        viewState = Loading
        val result = exercisesPresenter.getExercises()
        viewState = when(result) {
            is ResultSuccess -> {
                Loaded(result.value)
            }
            is ResultFailure -> {
                Failed(result.reason.message.toString())
            }
        }
    }

    fun deleteExercise(exercise: UiExercise) = execute {
        viewState = Loading
        when(val result = exercisesPresenter.deletExercise(exercise)) {
            is ResultSuccess -> {
                getExercises()
            }
            is ResultFailure -> {
                viewState = Failed(result.reason.message.toString())
            }
        }
    }

}