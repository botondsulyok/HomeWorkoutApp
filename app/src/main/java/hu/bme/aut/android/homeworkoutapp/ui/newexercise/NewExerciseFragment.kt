package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
class NewExerciseFragment : RainbowCakeFragment<NewExerciseViewState, NewExerciseViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    companion object {
        const val RC_VIDEO_CAPTURE = 100
    }

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

    private val exercise: UiNewExercise
        get() {
            return UiNewExercise(name = binding.etName.text.toString())
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAttachVideo.setOnClickListener {
            if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                captureVideoWithPermissionCheck()
            }
        }

        binding.btnCreate.setOnClickListener {
            if(binding.etName.text?.isEmpty() == true) {
                binding.etName.error = "Add a name!"
                return@setOnClickListener
            }
            viewModel.addExercise(exercise)
        }

        val adapter = ArrayAdapter(
            requireContext(), R.layout.exercise_categories_list_item, resources.getStringArray(
                R.array.exercise_categories_entries
            )
        )
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

    @NeedsPermission(Manifest.permission.CAMERA)
    fun captureVideo() {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (takeVideoIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(takeVideoIntent, RC_VIDEO_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri: Uri? = data?.data
            //videoView.setVideoURI(videoUri)
        }
    }

}