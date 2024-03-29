package hu.bme.aut.android.homeworkoutapp.core.testviewmodels

import hu.bme.aut.android.homeworkoutapp.ui.workouts.Loaded
import hu.bme.aut.android.homeworkoutapp.ui.workouts.WorkoutsViewModelBase
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import javax.inject.Inject

class TestWorkoutsViewModel @Inject constructor(
    private val workoutsList: List<UiWorkout>
)
    : WorkoutsViewModelBase() {

    override fun loadWorkouts() = execute {
        viewState = Loaded(workoutsList)
    }

    override fun deleteWorkout(workout: UiWorkout) = execute {
        // do nothing
    }

}