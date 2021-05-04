package hu.bme.aut.android.homeworkoutapp.ui.workouts

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentWorkoutsBinding
import hu.bme.aut.android.homeworkoutapp.ui.ActionFailed
import hu.bme.aut.android.homeworkoutapp.ui.ActionSuccess
import hu.bme.aut.android.homeworkoutapp.ui.workouts.models.UiWorkout
import hu.bme.aut.android.homeworkoutapp.ui.workouts.recyclerview.WorkoutsRecyclerViewAdapter


class WorkoutsFragment : RainbowCakeFragment<WorkoutsViewState, WorkoutsViewModelBase>(), WorkoutsRecyclerViewAdapter.WorkoutItemClickListener {

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
            findNavController().navigate(action)
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
        viewModel.loadWorkouts()
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

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is ActionFailed -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(activity, event.message, Toast.LENGTH_LONG).show()
            }
            is ActionSuccess -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemClick(workout: UiWorkout?): Boolean {
        if(workout != null) {
            val action = WorkoutsFragmentDirections.actionNavigationWorkoutsToWorkoutFragment(workout)
            findNavController().navigate(action)
        }
        return true
    }

    override fun onItemLongClick(workout: UiWorkout?): Boolean {
        if(workout != null) {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.title_warning))
                .setMessage(getString(R.string.txt_sure_to_delete))
                .setPositiveButton(getString(R.string.btn_yes)) { dialogInterface: DialogInterface, i: Int ->
                    viewModel.deleteWorkout(workout)
                }
                .setNegativeButton(getString(R.string.btn_no), null)
                .show()
        }
        return true
    }

}