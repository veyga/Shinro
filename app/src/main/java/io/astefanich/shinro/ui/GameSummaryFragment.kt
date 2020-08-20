package io.astefanich.shinro.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.astefanich.shinro.R
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.PlayRequest
import io.astefanich.shinro.databinding.FragmentGameSummaryBinding
import io.astefanich.shinro.viewmodels.GameSummaryViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import javax.inject.Inject

class GameSummaryFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var difficultiesReprs: Array<String>

    @Inject
    lateinit var changeDifficultyPrompt: String

    private lateinit var viewModel: GameSummaryViewModel
    private lateinit var binding: FragmentGameSummaryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game_summary, container, false)
        val gameOverFragmentArgs by navArgs<GameSummaryFragmentArgs>()
        var gameSummary = gameOverFragmentArgs.gameSummary

        (activity as MainActivity)
            .getMainActivityComponent()
            .getGameSummaryComponentBuilder()
            .difficulty(gameSummary.difficulty)
            .win(gameSummary.isWin)
            .time(gameSummary.time)
            .build()
            .inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameSummaryViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.newGameChip.typeface = Typeface.DEFAULT_BOLD
        binding.newGameChip.setOnClickListener {
            findNavController().navigate(
                GameSummaryFragmentDirections.actionGameSummaryToGame(
                    PlayRequest.NewGame(viewModel.nextGameDifficulty.value!!)
                )
            )
        }

        binding.changeDifficultyChip.typeface = Typeface.DEFAULT_BOLD
        binding.changeDifficultyChip.setOnClickListener {
            AlertDialog.Builder(activity)
                .setTitle(changeDifficultyPrompt)
                .setCancelable(true)
                .setItems(difficultiesReprs, DialogInterface.OnClickListener { dialog, choice ->
                    viewModel.difficultyChanged(Difficulty.valueOf(difficultiesReprs[choice]))
                })
                .show()
        }

        return binding.root
//        return layoutInflater.inflate(R.layout.fragment_game_summary, container, false)

    }
}