package hu.bme.aut.android.homeworkoutapp.ui.newexercise

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gusakov.library.start
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentRecordNewExerciseBinding
import hu.bme.aut.android.homeworkoutapp.ui.newexercise.models.UiNewExercise
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RecordNewExerciseFragment : Fragment(), LifecycleOwner {

    private var _binding: FragmentRecordNewExerciseBinding? = null
    private val binding get() = _binding!!

    private val args: RecordNewExerciseFragmentArgs by navArgs()

    private var speechRecognizer: SpeechRecognizer? = null

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    private var videoCapture: VideoCapture? = null

    private var cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    private var exercise = UiNewExercise()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exercise = args.exercise

        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity)
            speechRecognizer?.setRecognitionListener(ExerciseKeywordRecognitionListener())
        }
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
            if(binding.btnCapture.text == "Start Recording") {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
                binding.pulseCountDownRecordExercise.start {
                    startRecording()
                    //todo error az lehet a gond, hogy közben fut a kamera, ami hangot is rögzít és egyszerre nem fér hozzá a kettő (camerax+speech recognition)
                    startSpeechRecognizer()
                }
            }
            else {
                stopRecording()
            }
        }

    }

    private fun startSpeechRecognizer() {
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context?.packageName)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
            }
        }
        speechRecognizer?.startListening(recognizerIntent)
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
                .setBitRate(2_800_000)
                .setMaxResolution(Size(2000, 2000))
                .build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, videoCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }


    @SuppressLint("RestrictedApi")
    private fun startRecording() {
        // Get a stable reference of the modifiable image capture use case
        val videoCapture = videoCapture ?: return

        val videoFile = File(
            requireContext().getDir("Captured", Context.MODE_PRIVATE),
            SimpleDateFormat(FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".mp4")

        videoCapture.startRecording(
            videoFile,
            ContextCompat.getMainExecutor(requireContext()),
            object : VideoCapture.OnVideoSavedCallback {
                override fun onVideoSaved(file: File) {
                    val savedUri = Uri.fromFile(videoFile)
                    val msg = "Video capture succeeded: $savedUri"
                    Toast.makeText(requireContext(), "Captured", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)

                    val mp: MediaPlayer = MediaPlayer.create(activity, savedUri)
                    val durationInMilliseconds = mp.duration
                    mp.release()
                    val action = RecordNewExerciseFragmentDirections.actionRecordNewExerciseFragmentToNewExerciseFragment(
                        exercise.copy(videoUri = savedUri, videoLengthInMilliseconds = durationInMilliseconds, reps = 1))
                    findNavController().navigate(action)
                }

                override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                    Log.e(TAG, "Video capture failed: $message", cause)
                }
            })

        binding.btnCapture.text = "Stop Recording"
    }

    @SuppressLint("RestrictedApi")
    private fun stopRecording() {
        videoCapture?.stopRecording()
        binding.btnCapture.text = "Start Recording"
    }

    override fun onDestroyView() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.stopListening()
        speechRecognizer?.destroy()
    }

    inner class ExerciseKeywordRecognitionListener : RecognitionListener {

        private val TAG = "TAG_SPEECH"

        override fun onReadyForSpeech(params: Bundle?) {
            Log.d(TAG, "onReadyForSpeech")
        }

        override fun onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech")
        }

        override fun onRmsChanged(rmsdB: Float) {
            Log.d(TAG, "onRmsChanged")
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            Log.d(TAG, "onBufferReceived")
        }

        override fun onEndOfSpeech() {
            Log.d(TAG, "onEndofSpeech")
        }

        override fun onError(error: Int) {
            Log.d(TAG, "error $error")
        }

        override fun onResults(results: Bundle?) {
            val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            Log.d(TAG, "onResults")
            if(data != null) {
                for(d in data) {
                    if(d.contains("stop", ignoreCase = true)) {
                        stopRecording()
                        return
                    }
                }
                startSpeechRecognizer()

                // todo
                /*data.firstOrNull {
                    it.contains("stop", ignoreCase = true)
                }?.let {
                    startSpeechRecognizer()
                }*/
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            Log.d(TAG, "onPartialResults")
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            Log.d(TAG, "onEvent $eventType")
        }

    }

}