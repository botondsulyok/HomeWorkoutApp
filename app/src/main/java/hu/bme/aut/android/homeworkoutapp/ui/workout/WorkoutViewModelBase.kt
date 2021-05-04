package hu.bme.aut.android.homeworkoutapp.ui.workout

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise

abstract class WorkoutViewModelBase : RainbowCakeViewModel<WorkoutViewState>(Loading) {
    abstract var workoutId: String

    abstract fun loadWorkoutExercises()

    abstract fun deleteWorkoutExercise(exercise: UiExercise)

    abstract fun updateWorkoutExercise(exercise: UiExercise)
}