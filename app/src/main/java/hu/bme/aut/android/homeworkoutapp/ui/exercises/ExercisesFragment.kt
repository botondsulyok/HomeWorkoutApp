package hu.bme.aut.android.homeworkoutapp.ui.exercises

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
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentExercisesBinding
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentWorkoutsBinding
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.exercises.recyclerview.ExercisesRecyclerViewAdapter


class ExercisesFragment : RainbowCakeFragment<ExercisesViewState, ExercisesViewModel>(), ExercisesRecyclerViewAdapter.ExerciseItemClickListener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    private val recyclerViewAdapter = ExercisesRecyclerViewAdapter()

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

        recyclerViewAdapter.exerciseClickListener = this
        binding.exercisesRecyclerView.adapter = recyclerViewAdapter

        binding.fabCreateExercise.setOnClickListener {
            val action = ExercisesFragmentDirections.actionNavigationExercisesToNewExerciseFragment()
            findNavController().navigate(action)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as? MainActivity
    }

    override fun onStart() {
        super.onStart()
        viewModel.getExercises()
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

    override fun onItemClick(exercise: UiExercise?): Boolean {
        // TODO
        return true
    }

    override fun onItemLongClick(exercise: UiExercise?): Boolean {
        if (exercise != null) {
            viewModel.deleteExercise(exercise)
        }
        return true
    }

}