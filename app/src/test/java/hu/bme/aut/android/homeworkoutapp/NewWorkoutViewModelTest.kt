package hu.bme.aut.android.homeworkoutapp

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.android.homeExerciseapp.NewExerciseViewModelTest
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.UploadFailed
import hu.bme.aut.android.homeworkoutapp.ui.UploadSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.NewWorkoutPresenter
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.NewWorkoutViewModel
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import hu.bme.aut.android.homeworkoutapp.utils.ResourcesHelper
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class NewWorkoutViewModelTest : ViewModelTest() {

    companion object {
        private val WORKOUT = UiNewWorkout("Workout0")
        private val FAILURE_REASON = Exception("Something went wrong")
        private const val MOCK_RESOURCES_HELPER_STRING = "MockResourcesHelperString"
    }

    private lateinit var viewModel: NewWorkoutViewModel

    private val mockResourcesHelper = mock<ResourcesHelper>()

    @Before
    fun initResourcesHelperMock() {
        whenever(mockResourcesHelper.getString(ArgumentMatchers.anyInt())) doReturn MOCK_RESOURCES_HELPER_STRING
    }

    @Test
    fun testAddWorkoutResultSuccess() = runBlockingTest {
        // Given
        val mockNewWorkoutPresenter = mock<NewWorkoutPresenter>()
        whenever(mockNewWorkoutPresenter.addWorkout(WORKOUT)) doReturn ResultSuccess(Unit)

        viewModel = NewWorkoutViewModel(mockNewWorkoutPresenter, mockResourcesHelper)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.addWorkout(WORKOUT)
            eventsObserver.assertObserved(UploadSuccess(MOCK_RESOURCES_HELPER_STRING))
        }
        verify(mockNewWorkoutPresenter).addWorkout(WORKOUT)
    }

    @Test
    fun testAddWorkoutResultFailed() = runBlockingTest {
        // Given
        val mockNewWorkoutPresenter = mock<NewWorkoutPresenter>()
        whenever(mockNewWorkoutPresenter.addWorkout(WORKOUT)) doReturn ResultFailure(FAILURE_REASON)

        viewModel = NewWorkoutViewModel(mockNewWorkoutPresenter, mockResourcesHelper)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.addWorkout(WORKOUT)
            eventsObserver.assertObserved(UploadFailed("${MOCK_RESOURCES_HELPER_STRING}: ${FAILURE_REASON.message}"))
        }
        verify(mockNewWorkoutPresenter).addWorkout(WORKOUT)
    }

}