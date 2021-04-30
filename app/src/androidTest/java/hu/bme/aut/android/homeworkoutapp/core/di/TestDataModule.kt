package hu.bme.aut.android.homeworkoutapp.core.di

import dagger.Module
import dagger.Provides
import hu.bme.aut.android.homeworkoutapp.WorkoutFragmentTest
import hu.bme.aut.android.homeworkoutapp.WorkoutsFragmentTest
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout

@Module
class TestDataModule {

    @Provides
    fun provideWorkoutsList(): List<UiWorkout> {
        return WorkoutsFragmentTest.workoutsList
    }

    @Provides
    fun provideExercisesList(): List<UiExercise> {
        return WorkoutFragmentTest.exercisesList
    }

}