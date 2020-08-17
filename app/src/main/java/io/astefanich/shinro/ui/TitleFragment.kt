package io.astefanich.shinro.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.FragmentTitleBinding
import io.astefanich.shinro.domain.PlayRequest

class TitleFragment  : Fragment() {

    private lateinit var binding: FragmentTitleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)

        binding.playResumeChip.setOnClickListener {
            findNavController().navigate(
                TitleFragmentDirections.actionTitleToGame(PlayRequest.Resume)
            )
        }

        binding.howToPlayTipsChip.setOnClickListener{
            findNavController().navigate(
                TitleFragmentDirections.actionTitleToTipsChoice()
            )
        }

        binding.aboutChip.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToAbout())
        }

        return binding.root
    }

    //text reverts from bold to normal when popping/exiting from fragment
    override fun onStart() {
        super.onStart()
        if(this::binding.isInitialized){
            binding.playResumeChip.typeface = Typeface.DEFAULT_BOLD
            binding.howToPlayTipsChip.typeface = Typeface.DEFAULT_BOLD
            binding.statisticsChip.typeface = Typeface.DEFAULT_BOLD
            binding.leaderboardChip.typeface = Typeface.DEFAULT_BOLD
            binding.aboutChip.typeface = Typeface.DEFAULT_BOLD
        }
    }

}