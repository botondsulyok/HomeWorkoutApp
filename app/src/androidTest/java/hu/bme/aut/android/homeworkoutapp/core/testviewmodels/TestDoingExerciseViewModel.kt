package hu.bme.aut.android.homeworkoutapp.core.testviewmodels

import hu.bme.aut.android.homeworkoutapp.ui.doingexercise.DoingExerciseViewModelBase
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise

class TestDoingExerciseViewModel : DoingExerciseViewModelBase() {

    override var exercises: List<UiExercise> = listOf()

    override var currentExerciseNumber: Int = 0

    override var initialDurationInMilliseconds: Int = 0

    override var initialReps: Int = 0

    override fun addExercises(e: List<UiExercise>) {

    }

    override fun nextExercise() {

    }

    override fun initExercise() {

    }

    override fun startExercise() {

    }

    override fun calculateReps(): Int {
        return 0
    }

}