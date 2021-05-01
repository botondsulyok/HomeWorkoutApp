package hu.bme.aut.android.homeworkoutapp.ui.newworkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentNewWorkoutBinding
import hu.bme.aut.android.homeworkoutapp.ui.newworkout.models.UiNewWorkout
import hu.bme.aut.android.homeworkoutapp.utils.hideKeyboard

class NewWorkoutFragment : RainbowCakeFragment<NewWorkoutViewState, NewWorkoutViewModelBase>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    private var _binding: FragmentNewWorkoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            if(binding.etTitle.text?.isEmpty() == true) {
                binding.etTitle.error = getString(R.string.error_add_a_name)
                return@setOnClickListener
            }
            val workout = UiNewWorkout(name = binding.etTitle.text.toString())
            viewModel.addWorkout(workout)
        }

    }


    override fun render(viewState: NewWorkoutViewState) {
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
                viewModel.toInitialState()
            }
            is UploadSuccess -> {
                findNavController().popBackStack()
                return
            }
        }.exhaustive
    }

}