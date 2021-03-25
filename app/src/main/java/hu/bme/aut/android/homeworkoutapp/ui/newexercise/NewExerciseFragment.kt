package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
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

    private var mainActivity: MainActivity? = null

    var exercise = UiNewExercise()

    private val updatedExercise: UiNewExercise
        get() {
            return exercise.copy(
                name = binding.etName.text.toString(),
                reps = binding.etReps.text.toInt(),
                duration = Duration(
                    binding.npDurationHours.value,
                    binding.npDurationMinutes.value,
                    binding.npDurationSeconds.value
                ),
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

        initDuration()
        setDuration()
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

        binding.etReps.apply {
            addTextChangedListener {
                if(updatedExercise.videoLength.getDurationInSeconds() !=  0 && hasFocus()) {
                    // saveInstanceState miatt
                    exercise = updatedExercise.copy(duration = calculateDuration())
                    setDuration()
                }
            }
        }

        binding.npDurationHours.setOnValueChangedListener { picker, oldVal, newVal ->
            if(updatedExercise.videoLength.getDurationInSeconds() !=  0) {
                binding.etReps.clearFocus()
                hideKeyboard()
                binding.etReps.setText(calculateReps().toString())
            }
        }
        binding.npDurationMinutes.setOnValueChangedListener { picker, oldVal, newVal ->
            if(updatedExercise.videoLength.getDurationInSeconds() !=  0) {
                binding.etReps.clearFocus()
                hideKeyboard()
                binding.etReps.setText(calculateReps().toString())
            }
        }
        binding.npDurationSeconds.setOnValueChangedListener { picker, oldVal, newVal ->
            if(updatedExercise.videoLength.getDurationInSeconds() !=  0) {
                binding.etReps.clearFocus()
                hideKeyboard()
                binding.etReps.setText(calculateReps().toString())
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

    private fun calculateDuration() =
        Duration.build(updatedExercise.reps * updatedExercise.videoLength.getDurationInSeconds())

    private fun calculateReps() =
        updatedExercise.duration.getDurationInSeconds() / updatedExercise.videoLength.getDurationInSeconds()

    private fun initDuration() {
        binding.npDurationHours.minValue = 0
        binding.npDurationHours.maxValue = 24
        binding.npDurationMinutes.minValue = 0
        binding.npDurationMinutes.maxValue = 59
        binding.npDurationSeconds.minValue = 0
        binding.npDurationSeconds.maxValue = 59
    }

    private fun setDuration() {
        // saveInstanceState miatt nem updatedExercise van
        binding.npDurationHours.value = exercise.duration.hours
        binding.npDurationMinutes.value = exercise.duration.minutes
        binding.npDurationSeconds.value = exercise.duration.seconds
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
            val durationInSeconds = mp.duration / 1000
            mp.release()

            exercise = exercise.copy(videoUri = data?.data, videoLength = Duration.build(durationInSeconds))

            setVideoPlayback()

            if(binding.etReps.text.toInt() == 0) {
                exercise = exercise.copy(reps = 1)
                binding.etReps.setText(exercise.reps.toString())
            }
            exercise = exercise.copy(duration = calculateDuration())
            setDuration()
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
    }

}