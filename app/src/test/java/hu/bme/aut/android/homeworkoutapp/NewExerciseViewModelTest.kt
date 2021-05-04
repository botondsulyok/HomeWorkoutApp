package hu.bme.aut.android.homeExerciseapp

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.UploadFailed
import hu.bme.aut.android.homeworkoutapp.ui.UploadSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.NewExercisePresenter
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.NewExerciseViewModel
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class NewExerciseViewModelTest : ViewModelTest() {

    companion object {
        private val EXERCISE = UiNewExercise("Exercise0")
        private val FAILURE_REASON = Exception("Something went wrong")
    }

    private lateinit var viewModel: NewExerciseViewModel

    @Test
    fun testAddExerciseResultSuccess() = runBlockingTest {
        // Given
        val mockNewExercisePresenter = mock<NewExercisePresenter>()
        whenever(mockNewExercisePresenter.addExercise(EXERCISE)) doReturn ResultSuccess(Unit)

        viewModel = NewExerciseViewModel(mockNewExercisePresenter)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.addExercise(EXERCISE)
            eventsObserver.assertObserved(UploadSuccess("Created"))
        }
        verify(mockNewExercisePresenter).addExercise(EXERCISE)
    }

    @Test
    fun testAddExerciseResultFailed() = runBlockingTest {
        // Given
        val mockNewExercisePresenter = mock<NewExercisePresenter>()
        whenever(mockNewExercisePresenter.addExercise(EXERCISE)) doReturn ResultFailure(FAILURE_REASON)

        viewModel = NewExerciseViewModel(mockNewExercisePresenter)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.addExercise(EXERCISE)
            eventsObserver.assertObserved(UploadFailed("Upload failed: ${FAILURE_REASON.message.toString()}"))
        }
        verify(mockNewExercisePresenter).addExercise(EXERCISE)
    }

}