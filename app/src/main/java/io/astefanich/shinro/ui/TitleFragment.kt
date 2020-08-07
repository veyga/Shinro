package io.astefanich.shinro.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.TitleFragmentBinding
import io.astefanich.shinro.domain.TipChoice

class TitleFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidSupportInjection.inject(this)

        val binding: TitleFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.title_fragment, container, false)


        binding.playButton.setOnClickListener {
            findNavController().navigate(
                TitleFragmentDirections.actionTitleToGame(
                    0
                )
            )
        }

        binding.howToPlayButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToTipChoice(TipChoice.GENERAL))
        }

        binding.yourProgressButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToProgress())
        }

        binding.aboutButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToAbout())
        }

        return binding.root
    }


}
