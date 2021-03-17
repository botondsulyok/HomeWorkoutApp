package hu.bme.aut.android.homeworkoutapp.ui.exercises.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentEditExerciseBinding
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise

class EditExerciseBottomSheetDialogFragment()
    : BottomSheetDialogFragment() {

    companion object {
        const val EDIT_EXERCISE = "EDIT_EXERCISE"
        private const val KEY_DURATION_VALUES = "100"
    }

    private var _binding: FragmentEditExerciseBinding? = null
    private val binding get() = _binding!!

    var exercise = UiExercise()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.npDurationHours.minValue = 0
        binding.npDurationHours.maxValue = 24
        binding.npDurationMinutes.minValue = 0
        binding.npDurationMinutes.maxValue = 59
        binding.npDurationSeconds.minValue = 0
        binding.npDurationSeconds.maxValue = 59

        if(savedInstanceState == null) {
            binding.npDurationHours.value = exercise.duration.hours
            binding.npDurationMinutes.value = exercise.duration.minutes
            binding.npDurationSeconds.value = exercise.duration.seconds
            binding.etReps.setText(exercise.reps.toString())
        }
        else {
            val durationList =
                savedInstanceState.getIntegerArrayList(KEY_DURATION_VALUES)
            binding.npDurationHours.value = durationList?.get(0) ?: 0
            binding.npDurationMinutes.value = durationList?.get(1) ?: 0
            binding.npDurationSeconds.value = durationList?.get(2) ?: 0
        }

        binding.btnCreate.setOnClickListener {

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList(
            KEY_DURATION_VALUES,
            arrayListOf(
                binding.npDurationHours.value,
                binding.npDurationMinutes.value,
                binding.npDurationSeconds.value))
    }


}