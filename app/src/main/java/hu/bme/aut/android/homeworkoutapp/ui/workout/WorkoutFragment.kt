package hu.bme.aut.android.homeworkoutapp.ui.workout

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
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
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentWorkoutBinding
import hu.bme.aut.android.homeworkoutapp.ui.doingexercise.DoingExerciseFragmentDirections
import hu.bme.aut.android.homeworkoutapp.ui.exercises.ExerciseListener
import hu.bme.aut.android.homeworkoutapp.ui.exercises.dialogfragments.EditExerciseBottomSheetDialogFragment
import hu.bme.aut.android.homeworkoutapp.ui.exercises.dialogfragments.StartExerciseBottomSheetDialogFragment
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.workout.recyclerview.WorkoutExercisesRecyclerViewAdapter

class WorkoutFragment : RainbowCakeFragment<WorkoutViewState, WorkoutViewModelBase>(), WorkoutExercisesRecyclerViewAdapter.ExerciseItemClickListener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    private val args: WorkoutFragmentArgs by navArgs()

    private val recyclerViewAdapter = WorkoutExercisesRecyclerViewAdapter()

    private var startExerciseBottomSheetDialogFragment = StartExerciseBottomSheetDialogFragment()

    private var editExerciseBottomSheetDialogFragment = EditExerciseBottomSheetDialogFragment()

    private var mainActivity: MainActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.workoutId = args.workout.id
    }

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

        binding.fabAddExercise.setOnClickListener {
            val currentState = viewModel.state.value
            if(currentState is Loaded) {
                val action = DoingExerciseFragmentDirections.actionGlobalDoingExerciseFragment(currentState.exercisesList.toTypedArray())
                findNavController().navigate(action)
            }
        }

        viewModel.getWorkoutExercises()

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
        mainActivity?.binding?.toolbar?.title = args.workout.name
    }

    override fun onItemClick(exercise: UiExercise?): Boolean {
        // TODO edit feature
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(exercise: UiExercise?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onDeleteClick(exercise: UiExercise?): Boolean {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.title_warning))
            .setMessage(getString(R.string.txt_sure_to_delete))
            .setPositiveButton(getString(R.string.btn_yes)) { dialogInterface: DialogInterface, i: Int ->
                if (exercise != null) {
                    viewModel.deleteWorkoutExercise(exercise)
                }
            }
            .setNegativeButton(getString(R.string.btn_no), null)
            .show()
        return true
    }

    override fun onStartClick(exercise: UiExercise?): Boolean {
        if(exercise != null && !startExerciseBottomSheetDialogFragment.isAdded) {
            activity?.supportFragmentManager?.let { fragmentManager ->
                startExerciseBottomSheetDialogFragment = StartExerciseBottomSheetDialogFragment()
                startExerciseBottomSheetDialogFragment.also {
                    val args = Bundle()
                    args.putParcelable(StartExerciseBottomSheetDialogFragment.EXERCISE_VALUE, exercise)
                    args.putSerializable(StartExerciseBottomSheetDialogFragment.SAVE_ACTION_VALUE, ExerciseListener(this::updateExercise))
                    it.arguments = args
                }.show(fragmentManager, StartExerciseBottomSheetDialogFragment.START_EXERCISE)
            }
        }
        return true
    }

    override fun onEditClick(exercise: UiExercise?): Boolean {
        if(exercise != null && !editExerciseBottomSheetDialogFragment.isAdded) {
            activity?.supportFragmentManager?.let { fragmentManager ->
                editExerciseBottomSheetDialogFragment = EditExerciseBottomSheetDialogFragment()
                editExerciseBottomSheetDialogFragment.also {
                    val args = Bundle()
                    args.putParcelable(EditExerciseBottomSheetDialogFragment.EXERCISE_VALUE, exercise)
                    args.putSerializable(EditExerciseBottomSheetDialogFragment.SAVE_ACTION_VALUE, ExerciseListener(this::updateExercise))
                    it.arguments = args
                }.show(fragmentManager, EditExerciseBottomSheetDialogFragment.EDIT_EXERCISE)
            }
        }
        return true
    }

    private fun updateExercise(exercise: UiExercise) {
        viewModel.updateWorkoutExercise(exercise)
    }

}