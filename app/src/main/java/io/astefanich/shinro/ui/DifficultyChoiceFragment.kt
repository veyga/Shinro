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
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.PlayRequest
import io.astefanich.shinro.databinding.FragmentDifficultyChoiceBinding

class DifficultyChoiceFragment : Fragment() {

    private var _binding: FragmentDifficultyChoiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_difficulty_choice, container, false)

        binding.easyChip.setOnClickListener { navForChoice(Difficulty.EASY) }
        binding.mediumChip.setOnClickListener { navForChoice(Difficulty.MEDIUM) }
        binding.hardChip.setOnClickListener { navForChoice(Difficulty.HARD) }
        binding.lifecycleOwner = this
        return binding.root
    }


    private fun navForChoice(choice: Difficulty) {
        findNavController().navigate(
            DifficultyChoiceFragmentDirections.actionDifficultyChoiceToGame(
                PlayRequest.NewGame(choice)
            )
        )
    }

    // neither chip style nor raw xml bolds text deterministically....
    override fun onStart() {
        super.onStart()
        binding.easyChip.typeface = Typeface.DEFAULT_BOLD
        binding.mediumChip.typeface = Typeface.DEFAULT_BOLD
        binding.hardChip.typeface = Typeface.DEFAULT_BOLD
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
