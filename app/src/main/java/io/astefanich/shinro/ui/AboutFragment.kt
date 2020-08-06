package io.astefanich.shinro.ui


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
import dagger.android.support.AndroidSupportInjection
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.AboutFragmentBinding
import javax.inject.Inject

class AboutFragment : Fragment() {

    @Inject
    lateinit var videoUri: Uri

    var videoPlaying = MutableLiveData<Boolean>()
    var videoStarted = MutableLiveData<Boolean>()
    private lateinit var videoView: VideoView
    private var videoPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidSupportInjection.inject(this)

        val binding: AboutFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.about_fragment, container, false
        )

        videoView = binding.instructionsVideo
        videoView.setVideoURI(videoUri)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            videoView.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE)
        }

        videoView.setOnCompletionListener {
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
}
