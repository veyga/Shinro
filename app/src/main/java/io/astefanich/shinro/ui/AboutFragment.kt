package io.astefanich.shinro.ui


import android.graphics.Typeface
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.FragmentAboutBinding
import io.astefanich.shinro.databinding.FragmentTitleBinding
import io.astefanich.shinro.di.activities.main.fragments.DaggerAboutComponent
import javax.inject.Inject

class AboutFragment : Fragment() {

    @Inject
    lateinit var videoUri: Uri

    var videoPlaying = MutableLiveData<Boolean>()
    var videoStarted = MutableLiveData<Boolean>()
    private lateinit var videoView: VideoView
    private var videoPosition = 0

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        DaggerAboutComponent
            .create()
            .inject(this)

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false)

        binding.videoPlaybackButton.setTypeface(Typeface.DEFAULT_BOLD)
        videoView = binding.instructionsVideo
        videoView.setVideoURI(videoUri)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            videoView.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE)
        }

        videoView.setOnCompletionListener {
            binding.instructionsVideo.visibility = View.GONE
            binding.videoImgPlaceholder.visibility = View.VISIBLE
            videoStarted.value = false
            videoPlaying.value = false
            videoPosition = 0
        }

        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

    fun onVideoButtonPress() {
        videoStarted.value = true
        videoView.let {
            if (it.isPlaying) {
                it.pause()
                videoPlaying.value = false
            } else {
                binding.videoImgPlaceholder.visibility = View.GONE
                binding.instructionsVideo.visibility = View.VISIBLE
                it.start()
                videoPlaying.value = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        videoView.seekTo(videoPosition)
    }

    override fun onPause() {
        super.onPause()
        videoView.pause()
        videoPosition = videoView.currentPosition
        videoPlaying.value = false
    }

    override fun onStart() {
        super.onStart()
        binding.videoPlaybackButton.setTextColor(resources.getColor(R.color.white))
        binding.videoPlaybackButton.typeface = Typeface.DEFAULT_BOLD
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
