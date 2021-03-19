package hu.bme.aut.android.homeworkoutapp.ui.doingexercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import hu.bme.aut.android.homeworkoutapp.MainActivity
import hu.bme.aut.android.homeworkoutapp.databinding.FragmentDoingExerciseBinding


class DoingExerciseFragment : Fragment() {

    private var _binding: FragmentDoingExerciseBinding? = null
    private val binding get() = _binding!!

    val args: DoingExerciseFragmentArgs by navArgs()

    private var mainActivity: MainActivity? = null

    private var player: SimpleExoPlayer? = null
    private var playWhenReady: Boolean = true
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



    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO hideSystemUi()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
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
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player?.release()
            player = null
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as? MainActivity
        mainActivity?.supportActionBar?.hide()
        mainActivity?.binding?.navView?.visibility = View.GONE
    }

}