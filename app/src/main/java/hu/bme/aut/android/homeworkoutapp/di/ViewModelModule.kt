package hu.bme.aut.android.homeworkoutapp.di

import androidx.lifecycle.ViewModel
import co.zsmb.rainbowcake.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hu.bme.aut.android.homeworkoutapp.ui.doingexercise.DoingExerciseViewModel
import hu.bme.aut.android.homeworkoutapp.ui.exercises.ExercisesViewModel
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.NewExerciseViewModel
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.NewWorkoutViewModel
import hu.bme.aut.android.homeworkoutapp.ui.welcome.WelcomeViewModel
import hu.bme.aut.android.homeworkoutapp.ui.workout.WorkoutViewModel
import hu.bme.aut.android.homeworkoutapp.ui.workoutpicker.WorkoutPickerViewModel
import hu.bme.aut.android.homeworkoutapp.ui.workouts.WorkoutsViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(WorkoutsViewModel::class)
    abstract fun bindWorkoutsViewModel(workoutsViewModel: WorkoutsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun bindWelcomeViewModel(welcomeViewModel: WelcomeViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(NewWorkoutViewModel::class)
    abstract fun bindNewWorkoutViewModel(newWorkoutViewModel: NewWorkoutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExercisesViewModel::class)
    abstract fun bindExercisesViewModel(exercisesViewModel: ExercisesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewExerciseViewModel::class)
    abstract fun bindNewExerciseViewModel(newExerciseViewModel: NewExerciseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DoingExerciseViewModel::class)
    abstract fun bindDoingExerciseViewModel(doingExerciseViewModel: DoingExerciseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WorkoutViewModel::class)
    abstract fun bindWorkoutViewModel(workoutViewModel: WorkoutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WorkoutPickerViewModel::class)
    abstract fun bindWorkoutPickerViewModel(workoutPickerViewModel: WorkoutPickerViewModel): ViewModel

}