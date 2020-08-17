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
import io.astefanich.shinro.databinding.FragmentTipsChoiceBinding
import io.astefanich.shinro.domain.TipChoice

class TipsChoiceFragment : Fragment() {

    lateinit var binding : FragmentTipsChoiceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tips_choice, container, false)

        // neither chip style nor raw xml bolds text deterministically....
        binding.howToPlayChip.typeface = Typeface.DEFAULT_BOLD
//        binding.howToPlayChip.setOnClickListener {
//            findNavController().navigate(
//                TipsChoiceFragmentDirections.actionTipsChoiceToTipsList(
//                    TipChoice.GENERAL
//                )
//            )
//        }

        binding.pathfinderChip.typeface = Typeface.DEFAULT_BOLD
//        binding.pathfinderChip.setOnClickListener {
//            findNavController().navigate(
//                TipsChoiceFragmentDirections.actionTipsChoiceToTipsList(
//                    TipChoice.PATHFINDER
//                )
//            )
//        }

        binding.blockerChip.typeface = Typeface.DEFAULT_BOLD
//        binding.blockerChip.setOnClickListener {
//            findNavController().navigate(
//                TipsChoiceFragmentDirections.actionTipsChoiceToTipsList(
//                    TipChoice.BLOCKER
//                )
//            )
//        }

        binding.pigeonholeChip.typeface = Typeface.DEFAULT_BOLD
//        binding.pigeonholeChip.setOnClickListener {
//            findNavController().navigate(
//                TipsChoiceFragmentDirections.actionTipsChoiceToTipsList(
//                    TipChoice.PIGEONHOLE
//                )
//            )
//        }
        return binding.root
    }


    fun navToChoice(choice: TipChoice)  {
        findNavController().navigate(
            TipsChoiceFragmentDirections.actionTipsChoiceToTipsList(choice)
        )
    }
}
