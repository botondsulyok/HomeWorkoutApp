package hu.bme.aut.android.homeworkoutapp.ui.exercises

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentExercisesBinding
import hu.bme.aut.android.homeworkoutapp.ui.exercises.dialogfragments.StartExerciseBottomSheetDialogFragment
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.exercises.recyclerview.ExercisesRecyclerViewAdapter
import java.io.Serializable


class ExercisesFragment : RainbowCakeFragment<ExercisesViewState, ExercisesViewModel>(), ExercisesRecyclerViewAdapter.ExerciseItemClickListener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    private lateinit var recyclerViewAdapter: ExercisesRecyclerViewAdapter

    private var startExerciseBottomSheetDialogFragment = StartExerciseBottomSheetDialogFragment()

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
            val action = ExercisesFragmentDirections.actionNavigationExercisesToNewExerciseFragment()
            findNavController().navigate(action)
        }

        viewModel.getExercises()

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

    override fun onItemLongClick(exercise: UiExercise?): Boolean {
        // TODO elemek átrendezése
        return true
    }

    override fun onDeleteClick(exercise: UiExercise?): Boolean {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.title_warning))
            .setMessage(getString(R.string.txt_sure_to_delet))
            .setPositiveButton(getString(R.string.btn_yes)) { dialogInterface: DialogInterface, i: Int ->
                if (exercise != null) {
                    viewModel.deleteExercise(exercise)
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

    private fun updateExercise(exercise: UiExercise) {
        viewModel.updateExercise(exercise)
    }

}

class ExerciseListener(val action: (UiExercise) -> Unit) : Serializable
