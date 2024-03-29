package hu.bme.aut.android.homeworkoutapp.ui.workoutpicker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentWorkoutPickerBinding
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workoutpicker.recyclerview.WorkoutPickerRecyclerViewAdapter
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import java.io.Serializable

class WorkoutPickerFragment : RainbowCakeFragment<WorkoutPickerViewState, WorkoutPickerViewModelBase>(), WorkoutPickerRecyclerViewAdapter.WorkoutItemClickListener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    private var _binding: FragmentWorkoutPickerBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    private val args: WorkoutPickerFragmentArgs by navArgs()

    private val recyclerViewAdapter = WorkoutPickerRecyclerViewAdapter()

    companion object {
        const val KEY_PICK_WORKOUT = "KEY_PICK_WORKOUT"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewAdapter.workoutClickListener = this
        binding.workoutPickerRecyclerView.adapter = recyclerViewAdapter

        viewModel.getWorkouts()

    }

    override fun render(viewState: WorkoutPickerViewState) {
        when(viewState) {
            is Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is Loaded -> {
                binding.progressBar.visibility = View.GONE
                recyclerViewAdapter.submitList(viewState.workoutsList)
                viewModel.toReadyState()
            }
            is Ready -> {
                binding.progressBar.visibility = View.GONE
            }
            is Failed -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(activity, viewState.message, Toast.LENGTH_LONG).show()
            }
        }.exhaustive
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as? MainActivity
        mainActivity?.binding?.navView?.visibility = View.GONE
    }

    override fun onItemClick(workout: UiWorkout?): Boolean {
        if(workout != null) {
            args.selectedAction.action(listOf(workout))
            findNavController().popBackStack()
        }
        return true
    }

    override fun onItemLongClick(workout: UiWorkout?): Boolean {
        // TODO select more than one item
        return true
    }


}

class WorkoutPickedListener(val action: (List<UiWorkout>) -> Unit) : Serializable