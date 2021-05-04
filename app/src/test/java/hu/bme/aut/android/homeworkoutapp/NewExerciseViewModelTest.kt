package hu.bme.aut.android.homeExerciseapp

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.android.homeworkoutapp.ExercisesViewModelTest
import hu.bme.aut.android.homeworkoutapp.NewWorkoutViewModelTest
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.UploadFailed
import hu.bme.aut.android.homeworkoutapp.ui.UploadSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.NewExercisePresenter
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.NewExerciseViewModel
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
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
class NewExerciseViewModelTest : ViewModelTest() {

    companion object {
        private val EXERCISE = UiNewExercise("Exercise0")
        private val FAILURE_REASON = Exception("Something went wrong")
        private const val MOCK_RESOURCES_HELPER_STRING = "MockResourcesHelperString"
    }

    private lateinit var viewModel: NewExerciseViewModel

    private val mockResourcesHelper = mock<ResourcesHelper>()

    @Before
    fun initResourcesHelperMock() {
        whenever(mockResourcesHelper.getString(ArgumentMatchers.anyInt())) doReturn MOCK_RESOURCES_HELPER_STRING
    }

    @Test
    fun testAddExerciseResultSuccess() = runBlockingTest {
        // Given
        val mockNewExercisePresenter = mock<NewExercisePresenter>()
        whenever(mockNewExercisePresenter.addExercise(EXERCISE)) doReturn ResultSuccess(Unit)

        viewModel = NewExerciseViewModel(mockNewExercisePresenter, mockResourcesHelper)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.addExercise(EXERCISE)
            eventsObserver.assertObserved(UploadSuccess(MOCK_RESOURCES_HELPER_STRING))
        }
        verify(mockNewExercisePresenter).addExercise(EXERCISE)
    }

    @Test
    fun testAddExerciseResultFailed() = runBlockingTest {
        // Given
        val mockNewExercisePresenter = mock<NewExercisePresenter>()
        whenever(mockNewExercisePresenter.addExercise(EXERCISE)) doReturn ResultFailure(FAILURE_REASON)

        viewModel = NewExerciseViewModel(mockNewExercisePresenter, mockResourcesHelper)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.addExercise(EXERCISE)
            eventsObserver.assertObserved(UploadFailed("${MOCK_RESOURCES_HELPER_STRING}: ${FAILURE_REASON.message}"))
        }
        verify(mockNewExercisePresenter).addExercise(EXERCISE)
    }

}