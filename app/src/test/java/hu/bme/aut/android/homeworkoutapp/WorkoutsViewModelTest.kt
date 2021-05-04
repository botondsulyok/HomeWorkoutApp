package hu.bme.aut.android.homeworkoutapp

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.workouts.*
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class WorkoutsViewModelTest : ViewModelTest() {

    companion object {
        private val WORKOUTS = List(3) {
            UiWorkout(it.toString(), "Workout${it}")
        }
        private val FAILURE_REASON = Exception("Something went wrong")
    }

    private lateinit var viewModel: WorkoutsViewModel

    @Test
    fun testLoadWorkoutsResultSuccess() = runBlockingTest {
        // Given
        val mockWorkoutPresenter = mock<WorkoutsPresenter>()
        whenever(mockWorkoutPresenter.getWorkouts()) doReturn ResultSuccess(value = WORKOUTS)

        viewModel = WorkoutsViewModel(mockWorkoutPresenter)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.loadWorkouts()
            stateObserver.assertObserved(Loading, Loaded(WORKOUTS))
        }
        verify(mockWorkoutPresenter).getWorkouts()
    }

    @Test
    fun testLoadWorkoutsResultFailed() = runBlockingTest {
        // Given
        val mockWorkoutPresenter = mock<WorkoutsPresenter>()
        whenever(mockWorkoutPresenter.getWorkouts()) doReturn ResultFailure(FAILURE_REASON)

        viewModel = WorkoutsViewModel(mockWorkoutPresenter)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.loadWorkouts()
            stateObserver.assertObserved(Loading, Failed(FAILURE_REASON.message.toString()))
        }
        verify(mockWorkoutPresenter).getWorkouts()
    }

    @Test
    fun testDeleteWorkoutResultSuccess() = runBlockingTest {
        // Given
        val mockWorkoutPresenter = mock<WorkoutsPresenter>()
        whenever(mockWorkoutPresenter.deleteWorkout(WORKOUTS[0])) doReturn ResultSuccess(Unit)
        whenever(mockWorkoutPresenter.getWorkouts()) doReturn ResultSuccess(value = WORKOUTS)

        viewModel = WorkoutsViewModel(mockWorkoutPresenter)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.deleteWorkout(WORKOUTS[0])
            stateObserver.assertObserved(Loading, Loaded(WORKOUTS))
        }
        verify(mockWorkoutPresenter).deleteWorkout(WORKOUTS[0])
        verify(mockWorkoutPresenter).getWorkouts()
    }

}