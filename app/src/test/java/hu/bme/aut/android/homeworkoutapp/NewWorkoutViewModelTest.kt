package hu.bme.aut.android.homeworkoutapp

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.UploadFailed
import hu.bme.aut.android.homeworkoutapp.ui.UploadSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.NewWorkoutPresenter
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.NewWorkoutViewModel
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
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
    }

    private lateinit var viewModel: NewWorkoutViewModel

    @Test
    fun testAddWorkoutResultSuccess() = runBlockingTest {
        // Given
        val mockNewWorkoutPresenter = mock<NewWorkoutPresenter>()
        whenever(mockNewWorkoutPresenter.addWorkout(WORKOUT)) doReturn ResultSuccess(Unit)

        viewModel = NewWorkoutViewModel(mockNewWorkoutPresenter)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.addWorkout(WORKOUT)
            eventsObserver.assertObserved(UploadSuccess("Created"))
        }
        verify(mockNewWorkoutPresenter).addWorkout(WORKOUT)
    }

    @Test
    fun testAddWorkoutResultFailed() = runBlockingTest {
        // Given
        val mockNewWorkoutPresenter = mock<NewWorkoutPresenter>()
        whenever(mockNewWorkoutPresenter.addWorkout(WORKOUT)) doReturn ResultFailure(FAILURE_REASON)

        viewModel = NewWorkoutViewModel(mockNewWorkoutPresenter)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.addWorkout(WORKOUT)
            eventsObserver.assertObserved(UploadFailed("Upload failed: ${FAILURE_REASON.message.toString()}"))
        }
        verify(mockNewWorkoutPresenter).addWorkout(WORKOUT)
    }

}