package hu.bme.aut.android.homeworkoutapp.ui.exercises

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise

abstract class ExercisesViewModelBase : RainbowCakeViewModel<ExercisesViewState>(Loading) {
    abstract fun getExercises()

    abstract fun deleteExercise(exercise: UiExercise)

    abstract fun updateExercise(exercise: UiExercise)

    abstract fun addExerciseToWorkout(
        exercise: UiExercise,
        workoutId: String
    )
}