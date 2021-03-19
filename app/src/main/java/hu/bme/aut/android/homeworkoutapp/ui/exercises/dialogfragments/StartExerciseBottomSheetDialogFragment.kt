package hu.bme.aut.android.homeworkoutapp.ui.exercises.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentStartExerciseBinding
import hu.bme.aut.android.homeworkoutapp.ui.doingexercise.DoingExerciseFragmentDirections
import hu.bme.aut.android.homeworkoutapp.ui.exercises.ExerciseListener
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import hu.bme.aut.android.homeworkoutapp.utils.toInt

class StartExerciseBottomSheetDialogFragment
    : BottomSheetDialogFragment() {

    companion object {
        const val START_EXERCISE = "START_EXERCISE"
        const val EXERCISE_VALUE = "100"
        const val SAVE_ACTION_VALUE = "101"
    }

    private var _binding: FragmentStartExerciseBinding? = null
    private val binding get() = _binding!!

    var exercise = UiExercise()

    private val updatedExercise: UiExercise
    get() {
        return exercise.copy(
            reps = binding.etReps.text.toInt(),
            duration = Duration(
                binding.npDurationHours.value,
                binding.npDurationMinutes.value,
                binding.npDurationSeconds.value
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exercise = if(savedInstanceState == null) {
            arguments?.getParcelable(EXERCISE_VALUE) ?: UiExercise()
        } else {
            savedInstanceState.getParcelable(EXERCISE_VALUE) ?: UiExercise()
        }

        binding.npDurationHours.minValue = 0
        binding.npDurationHours.maxValue = 24
        binding.npDurationMinutes.minValue = 0
        binding.npDurationMinutes.maxValue = 59
        binding.npDurationSeconds.minValue = 0
        binding.npDurationSeconds.maxValue = 59

        binding.npDurationHours.value = exercise.duration.hours
        binding.npDurationMinutes.value = exercise.duration.minutes
        binding.npDurationSeconds.value = exercise.duration.seconds
        binding.etReps.setText(exercise.reps.toString())

        binding.btnStart.setOnClickListener {
            if(binding.cbSave.isChecked) {
                (arguments?.getSerializable(SAVE_ACTION_VALUE) as? ExerciseListener)?.action?.invoke(updatedExercise)
            }
            val action = DoingExerciseFragmentDirections.actionGlobalDoingExerciseFragment(updatedExercise)
            findNavController().navigate(action)
            dismiss()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXERCISE_VALUE, updatedExercise)
    }

}