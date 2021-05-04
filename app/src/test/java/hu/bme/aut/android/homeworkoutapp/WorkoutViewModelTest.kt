package hu.bme.aut.android.homeworkoutapp

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.ActionFailed
import hu.bme.aut.android.homeworkoutapp.ui.ActionSuccess
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workout.*
import hu.bme.aut.android.homeworkoutapp.utils.ResourcesHelper
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
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
        private const val MOCK_RESOURCES_HELPER_STRING = "MockResourcesHelperString"
    }

    private lateinit var viewModel: WorkoutViewModel

    private val mockResourcesHelper = mock<ResourcesHelper>()

    @Before
    fun initResourcesHelperMock() {
        whenever(mockResourcesHelper.getString(ArgumentMatchers.anyInt())) doReturn MOCK_RESOURCES_HELPER_STRING
    }

    @Test
    fun testLoadWorkoutExercisesResultSuccess() = runBlockingTest {
        // Given
        val mockWorkoutPresenter = mock<WorkoutPresenter>()
        whenever(mockWorkoutPresenter.getWorkoutExercises(WORKOUT_ID)) doReturn ResultSuccess(value = WORKOUT_EXERCISES)

        viewModel = WorkoutViewModel(mockWorkoutPresenter, mockResourcesHelper)
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

        viewModel = WorkoutViewModel(mockWorkoutPresenter, mockResourcesHelper)
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

        viewModel = WorkoutViewModel(mockWorkoutPresenter, mockResourcesHelper)
        viewModel.workoutId = WORKOUT_ID

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.deleteWorkoutExercise(WORKOUT_EXERCISES[0])
            stateObserver.assertObserved(Loading, Loaded(WORKOUT_EXERCISES))
            eventsObserver.assertObserved(ActionSuccess(MOCK_RESOURCES_HELPER_STRING))
        }
        verify(mockWorkoutPresenter).deleteWorkoutExercise(WORKOUT_ID, WORKOUT_EXERCISES[0])
        verify(mockWorkoutPresenter).getWorkoutExercises(WORKOUT_ID)
    }

    @Test
    fun testDeleteWorkoutExerciseResultFailed() = runBlockingTest {
        // Given
        val mockWorkoutPresenter = mock<WorkoutPresenter>()
        whenever(mockWorkoutPresenter.deleteWorkoutExercise(WORKOUT_ID, WORKOUT_EXERCISES[0])) doReturn ResultFailure(FAILURE_REASON)
        whenever(mockWorkoutPresenter.getWorkoutExercises(WORKOUT_ID)) doReturn ResultSuccess(value = WORKOUT_EXERCISES)

        viewModel = WorkoutViewModel(mockWorkoutPresenter, mockResourcesHelper)
        viewModel.workoutId = WORKOUT_ID

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.deleteWorkoutExercise(WORKOUT_EXERCISES[0])
            stateObserver.assertObserved(Loading, Loaded(WORKOUT_EXERCISES))
            eventsObserver.assertObserved(ActionFailed(FAILURE_REASON.message.toString()))
        }
        verify(mockWorkoutPresenter).deleteWorkoutExercise(WORKOUT_ID, WORKOUT_EXERCISES[0])
        verify(mockWorkoutPresenter).getWorkoutExercises(WORKOUT_ID)
    }

}