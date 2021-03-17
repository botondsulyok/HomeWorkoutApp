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
import android.widget.*
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import com.bumptech.glide.Glide
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentNewExerciseBinding
import hu.bme.aut.android.homeworkoutapp.ui.exercises.models.UiExercise
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import hu.bme.aut.android.homeworkoutapp.utils.hideKeyboard
import hu.bme.aut.android.homeworkoutapp.utils.setAllEnabled
import hu.bme.aut.android.homeworkoutapp.utils.toInt
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
class NewExerciseFragment : RainbowCakeFragment<NewExerciseViewState, NewExerciseViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    companion object {
        private const val RC_VIDEO_CAPTURE = 100
        private const val KEY_NEW_EXERCISE = "101"
    }

    private var _binding: FragmentNewExerciseBinding? = null
    private val binding get() = _binding!!

    var exercise = UiNewExercise()

    private val updatedExercise: UiNewExercise
        get() {
            return exercise.copy(
                name = binding.etName.text.toString(),
                reps = binding.etReps.text.toInt(),
                duration = Duration(
                    binding.npDurationHours.value,
                    binding.npDurationMinutes.value,
                    binding.npDurationSeconds.value),
                categoryEntry = binding.autoCompleteTextViewExerciseCategories.text.toString()
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exercise = savedInstanceState?.getParcelable(KEY_NEW_EXERCISE) ?: UiNewExercise()
    }

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

        binding.npDurationHours.minValue = 0
        binding.npDurationHours.maxValue = 24
        binding.npDurationMinutes.minValue = 0
        binding.npDurationMinutes.maxValue = 59
        binding.npDurationSeconds.minValue = 0
        binding.npDurationSeconds.maxValue = 59

        binding.npDurationHours.value = exercise.duration.hours
        binding.npDurationMinutes.value = exercise.duration.minutes
        binding.npDurationSeconds.value = exercise.duration.seconds
        binding.etReps.setText(exercise.reps.toString())

        setVideoPlayback()

        binding.ibExercisePlay.setOnClickListener {
            binding.rlExerciseVideoThumbnail.visibility = View.GONE
            binding.vvExerciseVideo.visibility = View.VISIBLE
            binding.vvExerciseVideo.start()
        }

        binding.vvExerciseVideo.setOnCompletionListener {
            binding.rlExerciseVideoThumbnail.visibility = View.VISIBLE
            binding.vvExerciseVideo.visibility = View.GONE
        }

        binding.vvExerciseVideo.apply {
            setOnClickListener {
                if(isPlaying) {
                    pause()
                }
                else {
                    start()
                }
            }
        }

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
            viewModel.addExercise(updatedExercise)
        }

        val adapter = ArrayAdapter(
            requireContext(), R.layout.exercise_categories_list_item, resources.getStringArray(
                R.array.exercise_categories_entries
            )
        )
        binding.autoCompleteTextViewExerciseCategories.setAdapter(adapter)
    }

    override fun render(viewState: NewExerciseViewState) {
        when(viewState) {
            is Initial -> {
                binding.root.setAllEnabled(true)
                binding.progressBar.visibility = View.GONE
                binding.btnCreate.visibility = View.VISIBLE
            }
            is Uploading -> {
                hideKeyboard()
                binding.root.setAllEnabled(false)
                binding.progressBar.visibility = View.VISIBLE
                binding.btnCreate.visibility = View.INVISIBLE
            }
            is UploadFailed -> {
                Toast.makeText(activity, "Upload failed", Toast.LENGTH_LONG).show()
                viewModel.toInitialState()
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
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            exercise = exercise.copy(videoUri = data?.data)
            setVideoPlayback()
        }
    }

    private fun setVideoPlayback() {
        exercise.videoUri?.let {
            binding.vvExerciseVideo.setVideoURI(it)
            Glide.with(requireContext())
                .load(it)
                .into(binding.ivExerciseThumbnail)
            binding.rlExerciseVideo.visibility = View.VISIBLE
            binding.rlExerciseVideoThumbnail.visibility = View.VISIBLE
            binding.btnAttachVideo.text = "Change Video"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_NEW_EXERCISE, updatedExercise)
    }

}