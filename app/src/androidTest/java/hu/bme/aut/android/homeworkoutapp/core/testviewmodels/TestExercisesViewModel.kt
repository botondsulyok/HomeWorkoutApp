package hu.bme.aut.android.homeworkoutapp.core.testviewmodels

import hu.bme.aut.android.homeworkoutapp.ui.exercises.ExercisesViewModelBase
import hu.bme.aut.android.homeworkoutapp.ui.exercises.Loaded
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import javax.inject.Inject

class TestExercisesViewModel @Inject constructor(
    val exercisesList: List<UiExercise>
) : ExercisesViewModelBase() {

    override fun loadExercises() {
        viewState = Loaded(exercisesList)
    }

    override fun deleteExercise(exercise: UiExercise) {

    }

    override fun updateExercise(exercise: UiExercise) {

    }

    override fun addExerciseToWorkout(exercise: UiExercise, workoutId: String) {

    }

}