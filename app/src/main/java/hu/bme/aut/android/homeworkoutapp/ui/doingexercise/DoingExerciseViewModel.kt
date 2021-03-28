package hu.bme.aut.android.homeworkoutapp.ui.doingexercise

import android.os.CountDownTimer
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import javax.inject.Inject


class DoingExerciseViewModel @Inject constructor(
) : RainbowCakeViewModel<DoingExerciseViewState>(Initial(UiExercise())) {

    var initialReps = 0

    fun addExercise(e: UiExercise) = execute {
        viewState = Initial(e)
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
            return viewState.exercise.reps
        }
        val d = viewState.exercise.duration.getDurationInMilliseconds() / viewState.exercise.videoLengthInMilliseconds
        return if(d < initialReps) d+1 else d
    }


}