package hu.bme.aut.android.homeworkoutapp.core.di

import dagger.Module
import dagger.Provides
import hu.bme.aut.android.homeworkoutapp.WorkoutsFragmentTest
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout

@Module
class TestDataModule {

    @Provides
    fun provideWorkoutsList(): List<UiWorkout> {
        return MutableList(WorkoutsFragmentTest.listItemNumberInTest + 4) {
            UiWorkout(it.toString(), "Workout${it}")
        }.apply {
            this[WorkoutsFragmentTest.listItemNumberInTest] = WorkoutsFragmentTest.workoutInTest
        }
    }

    /*@Provides
    fun provideExercisesList(): List<UiExercise> {
        return MutableList(WorkoutsFragmentTest.listItemNumberInTest + 4) {
            UiWorkout(it.toString(), "Workout${it}")
        }.apply {
            this[WorkoutsFragmentTest.listItemNumberInTest] = WorkoutsFragmentTest.workoutInTest
        }
    }*/

}