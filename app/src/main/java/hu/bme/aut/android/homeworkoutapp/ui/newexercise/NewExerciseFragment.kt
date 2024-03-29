package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import com.bumptech.glide.Glide
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentNewExerciseBinding
import hu.bme.aut.android.homeworkoutapp.ui.UploadFailed
import hu.bme.aut.android.homeworkoutapp.ui.UploadSuccess
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import hu.bme.aut.android.homeworkoutapp.utils.Duration
import hu.bme.aut.android.homeworkoutapp.utils.hideKeyboard
import hu.bme.aut.android.homeworkoutapp.utils.setAllEnabled
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.Serializable


@RuntimePermissions
class NewExerciseFragment : RainbowCakeFragment<NewExerciseViewState, NewExerciseViewModelBase>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    companion object {
        private const val KEY_NEW_EXERCISE = "KEY_NEW_EXERCISE"
        private const val KEY_MOTION_LAYOUT = "KEY_MOTION_LAYOUT"
    }

    private var _binding: FragmentNewExerciseBinding? = null
    private val binding get() = _binding!!

    private val args: NewExerciseFragmentArgs by navArgs()

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
        exercise = if(savedInstanceState == null) {
            args.exercise
        }
        else {
            savedInstanceState.getParcelable(KEY_NEW_EXERCISE) ?: UiNewExercise()
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

        binding.etName.setText(exercise.name)
        binding.autoCompleteTextViewExerciseCategories.setText(exercise.categoryEntry)

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
                binding.etName.error = getString(R.string.error_add_a_name)
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

        setVideoPlayback()

        binding.exerciseDurationPicker.newExercise = exercise

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
        }.exhaustive
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is UploadFailed -> {
                Toast.makeText(activity, event.message, Toast.LENGTH_LONG).show()
            }
            is UploadSuccess -> {
                Toast.makeText(activity, event.message, Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
                return
            }
        }
    }

    @NeedsPermission(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO)
    fun captureVideo() {
        val action = NewExerciseFragmentDirections.actionNewExerciseFragmentToRecordNewExerciseFragment(updatedExercise)
        findNavController().navigate(action)
    }

    private fun setVideoPlayback() {
        if(exercise.videoUri != null) {
            binding.vvExerciseVideo.setVideoURI(exercise.videoUri)
            Glide.with(requireContext())
                .load(exercise.videoUri)
                .into(binding.ivExerciseThumbnail)
            binding.rlExerciseVideo.visibility = View.VISIBLE
            binding.rlExerciseVideoThumbnail.visibility = View.VISIBLE
            binding.btnAttachVideo.text = getString(R.string.btn_change_video)
        }
    }

    override fun onPause() {
        super.onPause()
        if(_binding != null) {
            exercise = updatedExercise
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_NEW_EXERCISE, exercise)
        if(_binding != null) {
            outState.putInt(KEY_MOTION_LAYOUT, binding.motionLayoutNewExercise.currentState)
        }
        else {
            outState.putInt(KEY_MOTION_LAYOUT, R.id.newExerciseRowSceneEnd)
        }
    }

}