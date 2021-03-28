package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
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
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentNewExerciseBinding
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import hu.bme.aut.android.homeworkoutapp.utils.hideKeyboard
import hu.bme.aut.android.homeworkoutapp.utils.setAllEnabled
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
class NewExerciseFragment : RainbowCakeFragment<NewExerciseViewState, NewExerciseViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    companion object {
        private const val RC_VIDEO_CAPTURE = 100
        private const val KEY_NEW_EXERCISE = "KEY_NEW_EXERCISE"
        private const val KEY_MOTION_LAYOUT = "KEY_MOTION_LAYOUT"
    }

    private var _binding: FragmentNewExerciseBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    var exercise = UiNewExercise()

    private val updatedExercise: UiNewExercise
        get() {
            val e = binding.exerciseDurationPicker.newExercise
            return exercise.copy(
                name = binding.etName.text.toString(),
                reps = e.reps,
                duration = e.duration,
                categoryEntry = binding.autoCompleteTextViewExerciseCategories.text.toString()
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null) {
            exercise = savedInstanceState.getParcelable(KEY_NEW_EXERCISE) ?: UiNewExercise()
        }
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

        if(savedInstanceState != null) {
            binding.motionLayoutNewExercise.transitionToState(savedInstanceState.getInt(KEY_MOTION_LAYOUT))
        }

        binding.exerciseDurationPicker.newExercise = exercise

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
            val e = updatedExercise
            viewModel.addExercise(e.copy(duration = Duration.buildFromMilliseconds(e.reps * e.videoLengthInMilliseconds)))
        }

        val adapter = ArrayAdapter(
            requireContext(), R.layout.exercise_categories_list_item, resources.getStringArray(
                R.array.exercise_categories_entries
            )
        )
        binding.autoCompleteTextViewExerciseCategories.setAdapter(adapter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as? MainActivity
        mainActivity?.supportActionBar?.show()
        mainActivity?.binding?.navView?.visibility = View.GONE
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
            val mp: MediaPlayer = MediaPlayer.create(activity, data?.data)
            val durationInMilliseconds = mp.duration
            mp.release()

            exercise = exercise.copy(videoUri = data?.data, videoLengthInMilliseconds = durationInMilliseconds)

            setVideoPlayback()

            binding.exerciseDurationPicker.newExercise =
                    if(binding.exerciseDurationPicker.exercise.reps == 0) {
                        updatedExercise.copy(reps = 1)
                    }
                    else {
                        updatedExercise
                    }

        }
    }

    private fun setVideoPlayback() {
        if(exercise.videoUri != null) {
            binding.vvExerciseVideo.setVideoURI(exercise.videoUri)
            Glide.with(requireContext())
                .load(exercise.videoUri)
                .into(binding.ivExerciseThumbnail)
            binding.rlExerciseVideo.visibility = View.VISIBLE
            binding.rlExerciseVideoThumbnail.visibility = View.VISIBLE
            binding.btnAttachVideo.text = "Change Video"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_NEW_EXERCISE, updatedExercise)
        outState.putInt(KEY_MOTION_LAYOUT, binding.motionLayoutNewExercise.currentState)
    }

}