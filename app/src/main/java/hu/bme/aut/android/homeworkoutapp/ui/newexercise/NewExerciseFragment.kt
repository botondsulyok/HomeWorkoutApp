package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentNewExerciseBinding
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import hu.bme.aut.android.homeworkoutapp.utils.hideKeyboard

class NewExerciseFragment : RainbowCakeFragment<NewExerciseViewState, NewExerciseViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    private var _binding: FragmentNewExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            if(binding.etName.text?.isEmpty() == true) {
                binding.etName.error = "Add a name!"
                return@setOnClickListener
            }
            val workout = UiNewExercise(name = binding.etName.text.toString())
            viewModel.addWorkout(workout)
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.exercise_categories_list_item,  resources.getStringArray(R.array.exercise_categories_array))
        (binding.textInputLayoutCategories.editText as? AutoCompleteTextView)?.setAdapter(adapter)


    }

    override fun render(viewState: NewExerciseViewState) {
        when(viewState) {
            is Initial -> {
                binding.progressBar.visibility = View.GONE
                binding.btnCreate.visibility = View.VISIBLE
            }
            is Uploading -> {
                hideKeyboard()
                binding.progressBar.visibility = View.VISIBLE
                binding.btnCreate.visibility = View.INVISIBLE
            }
            is UploadFailed -> {
                binding.progressBar.visibility = View.GONE
                binding.btnCreate.visibility = View.VISIBLE
                Toast.makeText(activity, "Upload failed", Toast.LENGTH_LONG).show()
            }
            is UploadSuccess -> {
                findNavController().popBackStack()
                return
            }
        }.exhaustive
    }

}