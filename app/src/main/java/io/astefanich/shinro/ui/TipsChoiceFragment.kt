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
import io.astefanich.shinro.common.TipChoice
import io.astefanich.shinro.databinding.FragmentTipsChoiceBinding
import timber.log.Timber

class TipsChoiceFragment : Fragment() {

    lateinit var binding: FragmentTipsChoiceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tips_choice, container, false)

        // neither chip style nor raw xml bolds text deterministically....
        binding.howToPlayChip.typeface = Typeface.DEFAULT_BOLD
        binding.howToPlayChip.setOnClickListener { navToChoice(TipChoice.HOWTOPLAY) }

        binding.pathfinderChip.typeface = Typeface.DEFAULT_BOLD
        binding.pathfinderChip.setOnClickListener { navToChoice(TipChoice.PATHFINDER) }

        binding.blockerChip.typeface = Typeface.DEFAULT_BOLD
        binding.blockerChip.setOnClickListener { navToChoice(TipChoice.BLOCKER) }

        binding.pigeonholeChip.typeface = Typeface.DEFAULT_BOLD
        binding.pigeonholeChip.setOnClickListener { navToChoice(TipChoice.PIGEONHOLE) }

        return binding.root
    }


    private fun navToChoice(choice: TipChoice) {
        findNavController().navigate(
            TipsChoiceFragmentDirections.actionTipsChoiceToTipsList(choice)
        )
    }
}
