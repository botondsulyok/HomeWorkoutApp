package hu.bme.aut.android.homeworkoutapp.ui.doingexercise

import android.os.CountDownTimer
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import javax.inject.Inject


class DoingExerciseViewModel @Inject constructor(
) : DoingExerciseViewModelBase() {

    override var exercises = listOf<UiExercise>()
    override var currentExerciseNumber = 0

    override var initialDurationInMilliseconds = 0
    override var initialReps = 0

    override fun addExercises(e: List<UiExercise>) = execute {
        exercises = e
        initExercise()
    }

    override fun nextExercise() = execute {
        if(currentExerciseNumber < exercises.size-1) {
            currentExerciseNumber++
            initExercise()
        }
        else {
            viewState = Exit
        }
    }

    override fun initExercise() {
        viewState = Ready(exercises[currentExerciseNumber])
        initialDurationInMilliseconds = viewState.exercise.duration.getDurationInMilliseconds()
        initialReps = viewState.exercise.reps
    }

    override fun startExercise() = execute {
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

    override fun calculateReps(): Int {
        if(viewState.exercise.videoLengthInMilliseconds == 0) {
            return viewState.exercise.duration.getDurationInMilliseconds() / (initialDurationInMilliseconds / initialReps)
        }
        return viewState.exercise.duration.getDurationInMilliseconds() / viewState.exercise.videoLengthInMilliseconds
        //return if(d < initialReps) d+1 else d
    }


}