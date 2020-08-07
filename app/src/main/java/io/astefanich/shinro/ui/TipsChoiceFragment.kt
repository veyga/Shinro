package io.astefanich.shinro.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.TipsChoiceFragmentBinding
import io.astefanich.shinro.domain.TipChoice

class TipsChoiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: TipsChoiceFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.tips_choice_fragment, container, false)

        binding.howToPlayChip.setOnClickListener {
            findNavController().navigate(
                TipsChoiceFragmentDirections.actionTipsChoiceToTips(
                    TipChoice.GENERAL
                )
            )
        }

        binding.pathfinderChip.setOnClickListener {
            findNavController().navigate(
                TipsChoiceFragmentDirections.actionTipsChoiceToTips(
                    TipChoice.PATHFINDER
                )
            )
        }

        binding.blockerChip.setOnClickListener {
            findNavController().navigate(
                TipsChoiceFragmentDirections.actionTipsChoiceToTips(
                    TipChoice.BLOCKER
                )
            )
        }
        binding.pigeonholeChip.setOnClickListener {
            findNavController().navigate(
                TipsChoiceFragmentDirections.actionTipsChoiceToTips(
                    TipChoice.PIGEONHOLE
                )
            )
        }
        return binding.root
    }


}
