package hu.bme.aut.android.homeworkoutapp

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workout.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class WorkoutViewModelTest : ViewModelTest() {

    companion object {
        private val WORKOUT_EXERCISES = List(3) {
            UiExercise(it.toString(), "Exercise${it}")
        }
        private const val WORKOUT_ID = "0"
        private val FAILURE_REASON = Exception("Something went wrong")
    }

    private lateinit var viewModel: WorkoutViewModel

    @Test
    fun testLoadWorkoutExercisesResultSuccess() = runBlockingTest {
        // Given
        val mockWorkoutPresenter = mock<WorkoutPresenter>()
        whenever(mockWorkoutPresenter.getWorkoutExercises(WORKOUT_ID)) doReturn ResultSuccess(value = WORKOUT_EXERCISES)

        viewModel = WorkoutViewModel(mockWorkoutPresenter)
        viewModel.workoutId = WORKOUT_ID

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.loadWorkoutExercises()
            stateObserver.assertObserved(Loading, Loaded(WORKOUT_EXERCISES))
        }
        verify(mockWorkoutPresenter).getWorkoutExercises(WORKOUT_ID)
    }

    @Test
    fun testLoadWorkoutExercisesResultFailed() = runBlockingTest {
        // Given
        val mockWorkoutPresenter = mock<WorkoutPresenter>()
        whenever(mockWorkoutPresenter.getWorkoutExercises(WORKOUT_ID)) doReturn ResultFailure(FAILURE_REASON)

        viewModel = WorkoutViewModel(mockWorkoutPresenter)
        viewModel.workoutId = WORKOUT_ID

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.loadWorkoutExercises()
            stateObserver.assertObserved(Loading, Failed(FAILURE_REASON.message.toString()))
        }
        verify(mockWorkoutPresenter).getWorkoutExercises(WORKOUT_ID)
    }

    @Test
    fun testDeleteWorkoutExerciseResultSuccess() = runBlockingTest {
        // Given
        val mockWorkoutPresenter = mock<WorkoutPresenter>()
        whenever(mockWorkoutPresenter.deleteWorkoutExercise(WORKOUT_ID, WORKOUT_EXERCISES[0])) doReturn ResultSuccess(Unit)
        whenever(mockWorkoutPresenter.getWorkoutExercises(WORKOUT_ID)) doReturn ResultSuccess(value = WORKOUT_EXERCISES)

        viewModel = WorkoutViewModel(mockWorkoutPresenter)
        viewModel.workoutId = WORKOUT_ID

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.deleteWorkoutExercise(WORKOUT_EXERCISES[0])
            stateObserver.assertObserved(Loading, Loaded(WORKOUT_EXERCISES))
        }
        verify(mockWorkoutPresenter).deleteWorkoutExercise(WORKOUT_ID, WORKOUT_EXERCISES[0])
        verify(mockWorkoutPresenter).getWorkoutExercises(WORKOUT_ID)
    }

}