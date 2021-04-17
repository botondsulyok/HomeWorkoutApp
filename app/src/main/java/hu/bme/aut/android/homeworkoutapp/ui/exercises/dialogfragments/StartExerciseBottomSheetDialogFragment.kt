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
        const val EXERCISE_VALUE = "EXERCISE_VALUE"
        const val SAVE_ACTION_VALUE = "SAVE_ACTION_VALUE"
    }

    private var _binding: FragmentStartExerciseBinding? = null
    private val binding get() = _binding!!

    var exercise = UiExercise()

    private val updatedExercise: UiExercise
    get() {
        val e = binding.exerciseDurationPicker.exercise
        return exercise.copy(
            reps = e.reps,
            duration = e.duration
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exercise = if(savedInstanceState == null) {
            arguments?.getParcelable(EXERCISE_VALUE) ?: UiExercise()
        } else {
            savedInstanceState.getParcelable(EXERCISE_VALUE) ?: UiExercise()
        }
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

        binding.exerciseDurationPicker.exercise = exercise

        binding.btnStart.setOnClickListener {
            if(binding.cbSave.isChecked) {
                (arguments?.getSerializable(SAVE_ACTION_VALUE) as? ExerciseListener)?.action?.invoke(updatedExercise)
            }
            val action = DoingExerciseFragmentDirections.actionGlobalDoingExerciseFragment(arrayOf(updatedExercise))
            findNavController().navigate(action)
            dismiss()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXERCISE_VALUE, updatedExercise)
    }

}