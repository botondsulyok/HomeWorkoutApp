package hu.bme.aut.android.homeworkoutapp.ui.workouts

import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import javax.inject.Inject

class TestWorkoutsViewModel @Inject constructor()
    : WorkoutsViewModelBase() {

    override fun getWorkouts() = execute {
        viewState = Loaded(listOf(
            UiWorkout("0", "Workout0"),
            UiWorkout("1", "Workout1"),
            UiWorkout("2", "Workout2"),
            UiWorkout("3", "Workout3")
        ))
    }

    override fun deleteWorkout(workout: UiWorkout) = execute {
        // do nothing
    }

}