package hu.bme.aut.android.homeworkoutapp.core.di

import androidx.lifecycle.ViewModel
import co.zsmb.rainbowcake.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hu.bme.aut.android.homeworkoutapp.core.testviewmodels.*

@Module
abstract class TestViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TestWorkoutsViewModel::class)
    abstract fun bindWorkoutsViewModel(workoutsViewModel: TestWorkoutsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TestWelcomeViewModel::class)
    abstract fun bindWelcomeViewModel(welcomeViewModel: TestWelcomeViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TestNewWorkoutViewModel::class)
    abstract fun bindNewWorkoutViewModel(newWorkoutViewModel: TestNewWorkoutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TestExercisesViewModel::class)
    abstract fun bindExercisesViewModel(exercisesViewModel: TestExercisesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TestNewExerciseViewModel::class)
    abstract fun bindNewExerciseViewModel(newExerciseViewModel: TestNewExerciseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TestDoingExerciseViewModel::class)
    abstract fun bindDoingExerciseViewModel(doingExerciseViewModel: TestDoingExerciseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TestWorkoutViewModel::class)
    abstract fun bindWorkoutViewModel(workoutViewModel: TestWorkoutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TestWorkoutPickerViewModel::class)
    abstract fun bindWorkoutPickerViewModel(workoutPickerViewModel: TestWorkoutPickerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TestPlannedWorkoutsViewModel::class)
    abstract fun bindPlannedWorkoutsViewModel(plannedWorkoutsViewModel: TestPlannedWorkoutsViewModel): ViewModel

}