package hu.bme.aut.android.homeworkoutapp.ui.doexercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentDoExerciseBinding
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentExercisesBinding

class DoExerciseFragment : Fragment() {

    private var _binding: FragmentDoExerciseBinding? = null
    private val binding get() = _binding!!

    val args: DoExerciseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

}