package hu.bme.aut.android.homeworkoutapp.core.testviewmodels

import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workout.Loaded
import hu.bme.aut.android.homeworkoutapp.ui.workout.WorkoutViewModelBase
import javax.inject.Inject

class TestWorkoutViewModel @Inject constructor(
    private val exercisesList: List<UiExercise>
) : WorkoutViewModelBase() {

    override var workoutId: String = "0"

    override fun loadWorkoutExercises() {
        viewState = Loaded(exercisesList)
    }

    override fun deleteWorkoutExercise(exercise: UiExercise) {

    }

    override fun updateWorkoutExercise(exercise: UiExercise) {

    }

}