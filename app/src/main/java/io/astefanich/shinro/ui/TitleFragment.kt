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
import io.astefanich.shinro.domain.TipChoice

class TitleFragment : Fragment() {

    private lateinit var binding: FragmentTitleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)

        binding.playChip.setOnClickListener {
            findNavController().navigate(
                TitleFragmentDirections.actionTitleToGame(
                    0
                )
            )
        }

        binding.howToPlayChip.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToTipChoice(TipChoice.GENERAL))
        }

        binding.yourProgressChip.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToProgress())
        }

        binding.aboutChip.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToAbout())
        }

        return binding.root
    }


    //text reverts from bold to normal, when popping/exiting back to fragment?
    override fun onStart() {
        super.onStart()
        if (this::binding.isInitialized) {
            binding.playChip.typeface = Typeface.DEFAULT_BOLD
            binding.howToPlayChip.typeface = Typeface.DEFAULT_BOLD
            binding.yourProgressChip.typeface = Typeface.DEFAULT_BOLD
            binding.aboutChip.typeface = Typeface.DEFAULT_BOLD
        }
    }

}
