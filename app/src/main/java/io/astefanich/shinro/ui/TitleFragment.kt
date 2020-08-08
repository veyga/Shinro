package io.astefanich.shinro.ui


import android.graphics.Typeface
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


        binding.playChip.setTypeface(Typeface.DEFAULT_BOLD)
        binding.playChip.setOnClickListener {
            findNavController().navigate(
                TitleFragmentDirections.actionTitleToGame(
                    0
                )
            )
        }

        binding.howToPlayChip.setTypeface(Typeface.DEFAULT_BOLD)
        binding.howToPlayChip.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToTipChoice(TipChoice.GENERAL))
        }

        binding.yourProgressChip.setTypeface(Typeface.DEFAULT_BOLD)
        binding.yourProgressChip.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToProgress())
        }

        binding.aboutChip.setTypeface(Typeface.DEFAULT_BOLD)
        binding.aboutChip.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToAbout())
        }

        return binding.root
    }


}
