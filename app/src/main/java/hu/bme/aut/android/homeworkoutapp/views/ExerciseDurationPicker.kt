package hu.bme.aut.android.homeworkoutapp.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import hu.bme.aut.android.homeworkoutapp.databinding.ExerciseDurationPickerViewBinding
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import hu.bme.aut.android.homeworkoutapp.utils.toInt
import kotlin.math.roundToInt

class ExerciseDurationPicker(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding: ExerciseDurationPickerViewBinding = ExerciseDurationPickerViewBinding.inflate(LayoutInflater.from(context), this)

    var exercise: UiExercise = UiExercise()
    set(value) {
        _exercise = value
        setDuration()
        binding.etReps.setText(_exercise.reps.toString())
        field = value
    }
    get() {
        return updatedExercise.copy(duration = calculateDuration())
    }

    var newExercise: UiNewExercise = UiNewExercise()
        set(value) {
            _exercise = value.toUiExercise()
            binding.etReps.setText(_exercise.reps.toString())
            if(_exercise.videoLengthInMilliseconds !=  0) {
                _exercise = value.toUiExercise().copy(duration = calculateDuration())
            }
            setDuration()
            field = value
        }
        get() {
            return updatedExercise.copy(duration = calculateDuration()).toUiNewExercise()
        }

    private var _exercise: UiExercise = UiExercise()

    private val updatedExercise: UiExercise
        get() {
            return _exercise.copy(
                    reps = binding.etReps.text.toInt(),
                    duration = Duration(
                            binding.npDurationHours.value,
                            binding.npDurationMinutes.value,
                            binding.npDurationSeconds.value
                    )
            )
        }

    val duration: Duration
        get() {
            return calculateDuration()
        }

    val reps: Int
        get() {
            return updatedExercise.reps
        }

    init {
        initDuration()
        initReps()
    }

    private fun initDuration() {
        binding.npDurationHours.minValue = 0
        binding.npDurationHours.maxValue = 24
        binding.npDurationMinutes.minValue = 0
        binding.npDurationMinutes.maxValue = 59
        binding.npDurationSeconds.minValue = 0
        binding.npDurationSeconds.maxValue = 59

        binding.npDurationHours.setOnValueChangedListener { picker, oldVal, newVal ->
            if(_exercise.videoLengthInMilliseconds !=  0) {
                binding.etReps.clearFocus()
                binding.etReps.setText(calculateReps().toString())
            }
        }
        binding.npDurationMinutes.setOnValueChangedListener { picker, oldVal, newVal ->
            if(_exercise.videoLengthInMilliseconds !=  0) {
                binding.etReps.clearFocus()
                binding.etReps.setText(calculateReps().toString())
            }
        }
        binding.npDurationSeconds.setOnValueChangedListener { picker, oldVal, newVal ->
            if(_exercise.videoLengthInMilliseconds !=  0) {
                binding.etReps.clearFocus()
                binding.etReps.setText(calculateReps().toString())
            }
        }

    }

    private fun initReps() {
        binding.etReps.apply {
            addTextChangedListener {
                if(_exercise.videoLengthInMilliseconds !=  0 && hasFocus()) {
                    _exercise = updatedExercise.copy(duration = calculateDuration())
                    setDuration()
                }
            }
        }
    }

    private fun calculateDuration(): Duration {
        return if(_exercise.videoLengthInMilliseconds == 0) {
            updatedExercise.duration
        }
        else {
            Duration.buildFromMilliseconds(updatedExercise.reps * updatedExercise.videoLengthInMilliseconds)
        }

    }

    private fun calculateReps(): Int {
        return if(_exercise.videoLengthInMilliseconds == 0) {
            updatedExercise.reps
        }
        else {
            (updatedExercise.duration.getDurationInMilliseconds() / updatedExercise.videoLengthInMilliseconds.toDouble()).roundToInt()
        }

    }


    private fun setDuration() {
        binding.npDurationHours.value = _exercise.duration.hours
        binding.npDurationMinutes.value = _exercise.duration.minutes
        binding.npDurationSeconds.value = _exercise.duration.seconds
    }


    private fun UiNewExercise.toUiExercise(): UiExercise {
        return UiExercise(
                name = name,
                reps = reps,
                duration = duration,
                videoLengthInMilliseconds = videoLengthInMilliseconds
        )
    }

    private fun UiExercise.toUiNewExercise(): UiNewExercise {
        return UiNewExercise(
                name = name,
                reps = reps,
                duration = duration,
                videoLengthInMilliseconds = videoLengthInMilliseconds
        )
    }

}
