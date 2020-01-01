package io.astefanich.shinro.ui


import android.net.Uri
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

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {


    @Inject
    lateinit var videoUri: Uri

    private lateinit var videoView: VideoView

    var videoPlaying = MutableLiveData<Boolean>()

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
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

    fun onVideoButtonPress() {
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

}
