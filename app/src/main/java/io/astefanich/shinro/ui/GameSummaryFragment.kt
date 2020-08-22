package io.astefanich.shinro.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import io.astefanich.shinro.R
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.PlayRequest
import io.astefanich.shinro.databinding.FragmentGameSummaryBinding
import io.astefanich.shinro.viewmodels.GameSummaryViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import timber.log.Timber
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
            .mainActivityComponent
            .getGameSummaryComponentBuilder()
            .gameSummary(gameSummary)
            .build()
            .inject(this)

        Timber.i("Game Summary fragment got reprs: $difficultiesReprs")
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(GameSummaryViewModel::class.java)
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

        setHasOptionsMenu(true)
        return binding.root
//        return layoutInflater.inflate(R.layout.fragment_game_summary, container, false)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu_home, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (NavigationUI.onNavDestinationSelected(item!!, requireView().findNavController())
                || super.onOptionsItemSelected(item))
    }
}