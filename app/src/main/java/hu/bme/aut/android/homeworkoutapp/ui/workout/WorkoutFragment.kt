package hu.bme.aut.android.homeworkoutapp.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentWorkoutBinding
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workout.recyclerview.WorkoutExercisesRecyclerViewAdapter

class WorkoutFragment : RainbowCakeFragment<WorkoutViewState, WorkoutViewModel>(), WorkoutExercisesRecyclerViewAdapter.ExerciseItemClickListener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    private val args: WorkoutFragmentArgs by navArgs()

    private val recyclerViewAdapter = WorkoutExercisesRecyclerViewAdapter()

    private var mainActivity: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.workout = args.workout

        recyclerViewAdapter.exerciseClickListener = this
        binding.workoutExercisesRecyclerView.adapter = recyclerViewAdapter

        viewModel.getWorkoutExercises(args.workout.id)

    }

    override fun render(viewState: WorkoutViewState) {
        when(viewState) {
            is Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is Loaded -> {
                binding.progressBar.visibility = View.GONE
                recyclerViewAdapter.submitList(viewState.exercisesList)
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
        mainActivity?.setToolbarAndBottomNavigationViewVisible(true)
    }

    override fun onItemClick(exercise: UiExercise?): Boolean {
        // TODO edit feature
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(exercise: UiExercise?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDeleteClick(exercise: UiExercise?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onStartClick(exercise: UiExercise?): Boolean {
        TODO("Not yet implemented")
    }


}