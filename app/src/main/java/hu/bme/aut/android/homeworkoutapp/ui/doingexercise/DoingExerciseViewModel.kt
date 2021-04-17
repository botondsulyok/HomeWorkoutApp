package hu.bme.aut.android.homeworkoutapp.ui.doingexercise

import android.os.CountDownTimer
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import javax.inject.Inject


class DoingExerciseViewModel @Inject constructor(
) : RainbowCakeViewModel<DoingExerciseViewState>(Initial(UiExercise())) {

    var exercises = listOf<UiExercise>()
    var currentExerciseNumber = 0

    var initialDurationInMilliseconds = 0
    var initialReps = 0

    fun addExercises(e: List<UiExercise>) = execute {
        exercises = e
        initExercise()
    }

    fun nextExercise() = execute {
        if(currentExerciseNumber < exercises.size-1) {
            currentExerciseNumber++
            initExercise()
        }
        else {
            viewState = Exit
        }
    }

    private fun initExercise() {
        viewState = Ready(exercises[currentExerciseNumber])
        initialDurationInMilliseconds = viewState.exercise.duration.getDurationInMilliseconds()
        initialReps = viewState.exercise.reps
    }

    fun startExercise() = execute {
        viewState = DoingExercise(viewState.exercise)
        object : CountDownTimer(viewState.exercise.duration.getDurationInMilliseconds().toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                viewState = DoingExercise(viewState.exercise.copy(duration = Duration.buildFromMilliseconds(millisUntilFinished.toInt())))
                viewState = DoingExercise(viewState.exercise.copy(reps = calculateReps()))
            }
            override fun onFinish() {
                viewState = Finished(viewState.exercise.copy(duration = Duration.buildFromMilliseconds(0), reps = 0))
            }
        }.start()
    }

    private fun calculateReps(): Int {
        if(viewState.exercise.videoLengthInMilliseconds == 0) {
            return viewState.exercise.duration.getDurationInMilliseconds() / (initialDurationInMilliseconds / initialReps)
        }
        return viewState.exercise.duration.getDurationInMilliseconds() / viewState.exercise.videoLengthInMilliseconds
        //return if(d < initialReps) d+1 else d
    }


}