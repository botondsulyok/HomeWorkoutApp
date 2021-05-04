package hu.bme.aut.android.homeworkoutapp

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import hu.bme.aut.android.homeworkoutapp.data.ResultFailure
import hu.bme.aut.android.homeworkoutapp.data.ResultSuccess
import hu.bme.aut.android.homeworkoutapp.ui.ActionFailed
import hu.bme.aut.android.homeworkoutapp.ui.ActionSuccess
import hu.bme.aut.android.homeworkoutapp.ui.exercises.*
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class ExercisesViewModelTest : ViewModelTest() {

    companion object {
        private val EXERCISES = List(3) {
            UiExercise(it.toString(), "Exercise${it}")
        }
        private val FAILURE_REASON = Exception("Something went wrong")
    }

    private lateinit var viewModel: ExercisesViewModel

    @Test
    fun testLoadExercisesResultSuccess() = runBlockingTest {
        // Given
        val mockExercisesPresenter = mock<ExercisesPresenter>()
        whenever(mockExercisesPresenter.getExercises()) doReturn ResultSuccess(value = EXERCISES)

        viewModel = ExercisesViewModel(mockExercisesPresenter)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.loadExercises()
            stateObserver.assertObserved(Loading, Loaded(EXERCISES))
        }
        verify(mockExercisesPresenter).getExercises()
    }

    @Test
    fun testLoadExercisesResultFailed() = runBlockingTest {
        // Given
        val mockExercisePresenter = mock<ExercisesPresenter>()
        whenever(mockExercisePresenter.getExercises()) doReturn ResultFailure(FAILURE_REASON)

        viewModel = ExercisesViewModel(mockExercisePresenter)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.loadExercises()
            stateObserver.assertObserved(Loading, Failed(FAILURE_REASON.message.toString()))
        }
        verify(mockExercisePresenter).getExercises()
    }

    @Test
    fun testDeleteExerciseResultSuccess() = runBlockingTest {
        // Given
        val mockExercisePresenter = mock<ExercisesPresenter>()
        whenever(mockExercisePresenter.deleteExercise(EXERCISES[0])) doReturn ResultSuccess(Unit)
        whenever(mockExercisePresenter.getExercises()) doReturn ResultSuccess(value = EXERCISES)

        viewModel = ExercisesViewModel(mockExercisePresenter)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.deleteExercise(EXERCISES[0])
            stateObserver.assertObserved(Loading, Loaded(EXERCISES))
        }
        verify(mockExercisePresenter).deleteExercise(EXERCISES[0])
        verify(mockExercisePresenter).getExercises()
    }

    @Test
    fun testDeleteExerciseResultFailed() = runBlockingTest {
        // Given
        val mockExercisePresenter = mock<ExercisesPresenter>()
        whenever(mockExercisePresenter.deleteExercise(EXERCISES[0])) doReturn ResultFailure(FAILURE_REASON)
        whenever(mockExercisePresenter.getExercises()) doReturn ResultSuccess(value = EXERCISES)

        viewModel = ExercisesViewModel(mockExercisePresenter)

        //When, Then
        viewModel.observeStateAndEvents { stateObserver, eventsObserver ->
            viewModel.deleteExercise(EXERCISES[0])
            stateObserver.assertObserved(Loading, Loaded(EXERCISES))
            eventsObserver.assertObserved(ActionFailed(FAILURE_REASON.message.toString()))
        }
        verify(mockExercisePresenter).deleteExercise(EXERCISES[0])
        verify(mockExercisePresenter).getExercises()
    }

}