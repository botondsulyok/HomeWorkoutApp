package hu.bme.aut.android.homeworkoutapp.di

import androidx.lifecycle.ViewModel
import co.zsmb.rainbowcake.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.NewWorkoutViewModel
import hu.bme.aut.android.homeworkoutapp.ui.welcome.WelcomeViewModel
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

}