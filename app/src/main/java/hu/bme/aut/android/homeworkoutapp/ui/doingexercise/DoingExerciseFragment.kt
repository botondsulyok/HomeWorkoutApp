package hu.bme.aut.android.homeworkoutapp.ui.doingexercise

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import com.gusakov.library.java.interfaces.OnCountdownCompleted
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.R
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentDoingExerciseBinding


class DoingExerciseFragment :
    RainbowCakeFragment<DoingExerciseViewState, DoingExerciseViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = 0

    private var _binding: FragmentDoingExerciseBinding? = null
    private val binding get() = _binding!!

    private val args: DoingExerciseFragmentArgs by navArgs()

    private var mainActivity: MainActivity? = null

    companion object {
        private const val KEY_CURRENT_WINDOW = "KEY_CURRENT_WINDOW"
        private const val KEY_PLAYBACK_POSITION = "KEY_PLAYBACK_POSITION"
    }

    private var player: SimpleExoPlayer? = null
    private var playWhenReady: Boolean = false
    private var currentWindow = 0
    private var playbackPosition: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoingExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(savedInstanceState == null) {
            viewModel.addExercise(args.exercise)

            binding.pulseCountDownDoingExercise.start {
                binding.motionLayoutDoingExercise.transitionToState(R.id.DoingExerciseEnd)
                binding.motionLayoutDoingExercise.addTransitionListener(object :
                    MotionLayout.TransitionListener {
                    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
                    override fun onTransitionChange(
                        p0: MotionLayout?,
                        p1: Int,
                        p2: Int,
                        p3: Float
                    ) {
                    }

                    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                        player?.play()
                        viewModel.startExercise()
                        p0?.removeTransitionListener(this)
                    }

                    override fun onTransitionTrigger(
                        p0: MotionLayout?,
                        p1: Int,
                        p2: Boolean,
                        p3: Float
                    ) {
                    }
                })
            }
        }
        else {
            binding.motionLayoutDoingExercise.transitionToState(R.id.DoingExerciseEnd)
            playWhenReady = true
            currentWindow = savedInstanceState.getInt(KEY_CURRENT_WINDOW)
            playbackPosition = savedInstanceState.getLong(KEY_PLAYBACK_POSITION)
        }

    }

    override fun render(viewState: DoingExerciseViewState) {
        binding.tvDoingExerciseHeader.text = getString(
            R.string.tv_duration_and_reps,
            viewState.exercise.duration.toString(),
            viewState.exercise.reps
        )
        when(viewState) {
            is Initial -> {

            }
            is DoingExercise -> {

            }
            is Finished -> {
                // TODO set background (attr)
            }
        }.exhaustive
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }

    private fun hideSystemUi() {
        binding.vvDoingExercise.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.vvDoingExercise.player = player

        val mediaItem: MediaItem = MediaItem.fromUri(args.exercise.videoUrl)
        player?.setMediaItem(mediaItem)

        player?.playWhenReady = playWhenReady
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare()

        player?.addListener(object : Player.EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                if(state == Player.STATE_ENDED) {
                    player?.seekTo(0)
                    player?.play()
                }
            }
        })
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as? MainActivity
        mainActivity?.setToolbarAndBottomNavigationViewVisible(false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_WINDOW, currentWindow)
        outState.putLong(KEY_PLAYBACK_POSITION, playbackPosition)
    }

}