package hu.bme.aut.android.homeworkoutapp.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentWorkoutsBinding
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.recyclerview.WorkoutsRecyclerViewAdapter


class WorkoutsFragment : RainbowCakeFragment<WorkoutsViewState, WorkoutsViewModel>(), WorkoutsRecyclerViewAdapter.WorkoutItemClickListener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    private var _binding: FragmentWorkoutsBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    private val recyclerViewAdapter = WorkoutsRecyclerViewAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewAdapter.workoutClickListener = this
        binding.workoutsRecyclerView.adapter = recyclerViewAdapter

        binding.btnCreateWorkout.setOnClickListener {
            val action = WorkoutsFragmentDirections.actionNavigationWorkoutsToNewWorkoutFragment()
            Navigation.findNavController(view).navigate(action)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as? MainActivity
        mainActivity?.supportActionBar?.show()
        mainActivity?.binding?.navView?.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        viewModel.getWorkouts()
    }

    override fun render(viewState: WorkoutsViewState) {
        when(viewState) {
            is Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is Loaded -> {
                binding.progressBar.visibility = View.GONE
                recyclerViewAdapter.submitList(viewState.workoutsList)
            }
            is Failed -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(activity, viewState.message, Toast.LENGTH_LONG).show()
            }
        }.exhaustive

    }

    override fun onItemClick(workout: UiWorkout?): Boolean {
        if(workout != null) {
            val action = WorkoutsFragmentDirections.actionNavigationWorkoutsToWorkoutFragment(workout)
            findNavController().navigate(action)
        }
        return true
    }

    override fun onItemLongClick(workout: UiWorkout?): Boolean {
        // TODO
        if(workout != null) {
            viewModel.deleteWorkout(workout)
        }
        return true
    }

}