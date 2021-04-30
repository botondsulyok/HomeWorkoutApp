package hu.bme.aut.android.homeworkoutapp.core.testviewmodels

import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workout.WorkoutViewModelBase

class TestWorkoutViewModel : WorkoutViewModelBase() {

    override var workoutId: String = "0"

    override fun getWorkoutExercises() {

    }

    override fun deleteWorkoutExercise(exercise: UiExercise) {

    }

    override fun updateWorkoutExercise(exercise: UiExercise) {

    }

}