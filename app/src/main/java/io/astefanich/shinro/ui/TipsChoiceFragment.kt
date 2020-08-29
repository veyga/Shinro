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

class TipsChoiceFragment : Fragment() {

    private var _binding: FragmentTipsChoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tips_choice, container, false)

        // neither chip style nor raw xml bolds text deterministically....
        binding.howToPlayChip.typeface = Typeface.DEFAULT_BOLD
        binding.howToPlayChip.setOnClickListener { navToChoice(TipChoice.HOWTOPLAY) }

        binding.pathfinderChip.typeface = Typeface.DEFAULT_BOLD
        binding.pathfinderChip.setOnClickListener { navToChoice(TipChoice.PATHFINDER) }

        binding.blockerChip.typeface = Typeface.DEFAULT_BOLD
        binding.blockerChip.setOnClickListener { navToChoice(TipChoice.BLOCKER) }

        binding.pigeonholeChip.typeface = Typeface.DEFAULT_BOLD
        binding.pigeonholeChip.setOnClickListener { navToChoice(TipChoice.PIGEONHOLE) }

        binding.lifecycleOwner = this
        return binding.root
    }


    private fun navToChoice(choice: TipChoice) {
        findNavController().navigate(
            TipsChoiceFragmentDirections.actionTipsChoiceToTipsList(choice)
        )
    }

    override fun onStart() {
        super.onStart()
        binding.howToPlayChip.setTextColor(resources.getColor(R.color.white))
        binding.pathfinderChip.setTextColor(resources.getColor(R.color.white))
        binding.blockerChip.setTextColor(resources.getColor(R.color.white))
        binding.pigeonholeChip.setTextColor(resources.getColor(R.color.white))
        binding.howToPlayChip.typeface = Typeface.DEFAULT_BOLD
        binding.pathfinderChip.typeface = Typeface.DEFAULT_BOLD
        binding.blockerChip.typeface = Typeface.DEFAULT_BOLD
        binding.pigeonholeChip.typeface = Typeface.DEFAULT_BOLD

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
