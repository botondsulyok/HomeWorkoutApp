package hu.bme.aut.android.homeworkoutapp.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


        //TODO
        val l = mutableListOf(UiWorkout("1", "Alma"))
        for(i in 0..100) {
            l.add(UiWorkout("${i + 1}", "Workout${i + 1}"))
        }
        recyclerViewAdapter.submitList(l)


        binding.btnCreateWorkout.setOnClickListener {
            val action = WorkoutsFragmentDirections.actionNavigationWorkoutsToNewWorkoutFragment()
            findNavController().navigate(action)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as? MainActivity
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadWorkouts()
    }

    override fun render(viewState: WorkoutsViewState) {
        when(viewState) {
            is Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is Loaded -> {
                binding.progressBar.visibility = View.GONE
            }
        }.exhaustive

    }

    override fun onItemClick(position: Int, view: View, uiWorkout: UiWorkout?, viewHolder: WorkoutsRecyclerViewAdapter.ViewHolder): Boolean {
        // TODO
        return true
    }

}