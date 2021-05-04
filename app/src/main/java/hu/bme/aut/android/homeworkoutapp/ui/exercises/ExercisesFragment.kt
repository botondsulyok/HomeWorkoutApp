package hu.bme.aut.android.homeworkoutapp.ui.exercises

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
import com.github.zawadz88.materialpopupmenu.popupMenu
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.ExercisesRowBinding
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentExercisesBinding
import hu.bme.aut.android.homeworkoutapp.ui.ActionFailed
import hu.bme.aut.android.homeworkoutapp.ui.ActionSuccess
import hu.bme.aut.android.homeworkoutapp.ui.UploadFailed
import hu.bme.aut.android.homeworkoutapp.ui.exercises.dialogfragments.EditExerciseBottomSheetDialogFragment
import hu.bme.aut.android.homeworkoutapp.ui.exercises.dialogfragments.StartExerciseBottomSheetDialogFragment
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.exercises.recyclerview.ExercisesRecyclerViewAdapter
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import hu.bme.aut.android.homeworkoutapp.ui.workoutpicker.WorkoutPickedListener
import java.io.Serializable


class ExercisesFragment : RainbowCakeFragment<ExercisesViewState, ExercisesViewModelBase>(), ExercisesRecyclerViewAdapter.ExerciseItemClickListener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    private lateinit var recyclerViewAdapter: ExercisesRecyclerViewAdapter

    private var startExerciseBottomSheetDialogFragment = StartExerciseBottomSheetDialogFragment()

    private var editExerciseBottomSheetDialogFragment = EditExerciseBottomSheetDialogFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewAdapter = ExercisesRecyclerViewAdapter(requireContext())
        recyclerViewAdapter.exerciseClickListener = this
        binding.exercisesRecyclerView.adapter = recyclerViewAdapter

        binding.fabCreateExercise.setOnClickListener {
            val action = ExercisesFragmentDirections.actionNavigationExercisesToNewExerciseFragment(
                UiNewExercise()
            )
            findNavController().navigate(action)
        }

        viewModel.loadExercises()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as? MainActivity
        mainActivity?.setToolbarAndBottomNavigationViewVisible(true)
    }

    override fun render(viewState: ExercisesViewState) {
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

    override fun onItemLongClick(exerciseRowBinding: ExercisesRowBinding?): Boolean {
        if(exerciseRowBinding != null) {
            val exercise = exerciseRowBinding.exercise
            if(exercise != null) {
                popupMenu {
                    section {
                        item {
                            labelRes = R.string.menu_add_exercise_to_workout
                            icon = R.drawable.ic_baseline_add_24
                            callback = {
                                val action = ExercisesFragmentDirections.actionNavigationExercisesToWorkoutPickerFragment(
                                    WorkoutPickedListener { workouts ->
                                        val ids = workouts.map { it.id }
                                        viewModel.addExerciseToWorkout(exercise, ids[0])
                                    })
                                findNavController().navigate(action)
                            }
                        }
                    }
                }.apply {
                    show(requireContext(), exerciseRowBinding.root)
                }
            }

        }
        return true
    }

    override fun onDeleteClick(exercise: UiExercise?): Boolean {
        if (exercise != null) {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.title_warning))
                .setMessage(getString(R.string.txt_sure_to_delete))
                .setPositiveButton(getString(R.string.btn_yes)) { dialogInterface: DialogInterface, i: Int ->
                    viewModel.deleteExercise(exercise)
                }
                .setNegativeButton(getString(R.string.btn_no), null)
                .show()
        }
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
        viewModel.updateExercise(exercise)
    }

}

class ExerciseListener(val action: (UiExercise) -> Unit) : Serializable
