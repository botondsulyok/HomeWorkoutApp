package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gusakov.library.start
import github.com.vikramezhil.dks.speech.Dks
import github.com.vikramezhil.dks.speech.DksListener
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentRecordNewExerciseBinding
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class RecordNewExerciseFragment : Fragment(), LifecycleOwner {

    private var _binding: FragmentRecordNewExerciseBinding? = null
    private val binding get() = _binding!!

    private val args: RecordNewExerciseFragmentArgs by navArgs()

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    private var videoCapture: VideoCapture? = null

    private var cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    private var exercise = UiNewExercise()

    private var dks: Dks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exercise = args.exercise
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordNewExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewFinder.post {
            startCamera()
        }

        binding.btnCapture.setOnClickListener {
            if(binding.btnCapture.text == getString(R.string.btn_start_recording)) {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
                binding.pulseCountDownRecordExercise.start {
                    startRecording()
                }
            }
            else {
                stopRecording()
            }
        }

        binding.btnSwitchCamera.setOnClickListener {
            cameraSelector = if(cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            else {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
            startCamera()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fun handleSpeechResult(result: String) {
            if(result.contains("stop", ignoreCase = true)) {
                stopRecording()
            }
            else if(result.contains("start", ignoreCase = true)) {
                startRecording()
            }
        }
        muteAudio(true, requireContext())
        dks = Dks(requireActivity().application, parentFragmentManager, object : DksListener {
            override fun onDksLiveSpeechResult(liveSpeechResult: String) {
                handleSpeechResult(liveSpeechResult)
                Log.d("DKS", "Speech result - $liveSpeechResult")
            }

            override fun onDksFinalSpeechResult(speechResult: String) {
                handleSpeechResult(speechResult)
                Log.d("DKS", "Final speech result - $speechResult")
            }

            override fun onDksLiveSpeechFrequency(frequency: Float) {
                Log.d("DKS", "frequency - $frequency")
            }

            override fun onDksLanguagesAvailable(
                defaultLanguage: String?,
                supportedLanguages: ArrayList<String>?
            ) {
                if (supportedLanguages != null && supportedLanguages.contains("en-US")) {
                    dks?.currentSpeechLanguage = "en-US"
                }
                Log.d("DKS", "defaultLanguage - $defaultLanguage")
                Log.d("DKS", "supportedLanguages - $supportedLanguages")
            }

            override fun onDksSpeechError(errMsg: String) {
                Log.d("DKS", "errMsg - $errMsg")
            }
        })
        dks?.startSpeechRecognition()
    }

    @SuppressLint("RestrictedApi")
    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.createSurfaceProvider())
                }

            videoCapture = VideoCapture
                .Builder()
                .setBitRate(4_800_000)
                .setMaxResolution(Size(2000, 2000))
                .build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, videoCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }


    @SuppressLint("RestrictedApi")
    private fun startRecording() {
        if(binding.btnCapture.text == getString(R.string.btn_stop_recording)) {
            return
        }

        // egyelőre laggol a videó, ha közben fut a hangfelismerés, ezért le kell állítani
        dks?.continuousSpeechRecognition = false
        dks?.closeSpeechOperations()

        binding.btnSwitchCamera.visibility = View.GONE
        binding.btnCapture.text = getString(R.string.btn_stop_recording)
        // Get a stable reference of the modifiable image capture use case
        val videoCapture = videoCapture ?: return

        val videoFile = File(
            requireContext().getDir("Captured", Context.MODE_PRIVATE),
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".mp4"
        )

        videoCapture.startRecording(
            videoFile,
            ContextCompat.getMainExecutor(requireContext()),
            object : VideoCapture.OnVideoSavedCallback {
                override fun onVideoSaved(file: File) {
                    val savedUri = Uri.fromFile(videoFile)
                    val msg = "Video capture succeeded: $savedUri"
                    Toast.makeText(requireContext(), getString(R.string.txt_captured), Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)

                    val mp: MediaPlayer = MediaPlayer.create(activity, savedUri)
                    val durationInMilliseconds = mp.duration
                    mp.release()
                    val action =
                        RecordNewExerciseFragmentDirections.actionRecordNewExerciseFragmentToNewExerciseFragment(
                            exercise.copy(
                                videoUri = savedUri,
                                videoLengthInMilliseconds = durationInMilliseconds,
                                reps = 1
                            )
                        )
                    findNavController().navigate(action)
                }

                override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                    Log.e(TAG, "Video capture failed: $message", cause)
                }
            })
    }

    @SuppressLint("RestrictedApi")
    private fun stopRecording() {
        if(binding.btnCapture.text == getString(R.string.btn_start_recording)) {
            return
        }
        binding.btnCapture.text = getString(R.string.btn_start_recording)
        videoCapture?.stopRecording()
        binding.btnSwitchCamera.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        muteAudio(false, requireContext())
        super.onDestroyView()
    }

    private fun muteAudio(shouldMute: Boolean, context: Context) {
        val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val muteValue = if (shouldMute) AudioManager.ADJUST_MUTE else AudioManager.ADJUST_UNMUTE
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, muteValue, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        dks?.closeSpeechOperations()
    }

}