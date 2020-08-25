package io.astefanich.shinro.ui

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import arrow.core.Option
import arrow.core.Some
import io.astefanich.shinro.R
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.PlayRequest
import io.astefanich.shinro.databinding.FragmentGameSummaryBinding
import io.astefanich.shinro.util.sound.SoundEffect
import io.astefanich.shinro.util.sound.SoundPlayer
import io.astefanich.shinro.viewmodels.GameSummaryViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class GameSummaryFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var difficultiesReprs: Array<String>

    @Inject
    @JvmSuppressWildcards
    @field:Named("gameSummarySoundPlayer")
    lateinit var soundplayer: Option<SoundPlayer>

    private lateinit var viewModel: GameSummaryViewModel

    private lateinit var binding: FragmentGameSummaryBinding

    private var animationFinished = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameSummaryFragmentArgs by navArgs<GameSummaryFragmentArgs>()
        (activity as MainActivity)
            .mainActivityComponent
            .getGameSummaryComponentBuilder()
            .gameSummary(gameSummaryFragmentArgs.gameSummary)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game_summary, container, false)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(GameSummaryViewModel::class.java)
        binding.vm = viewModel
        binding.newGameChip.typeface = Typeface.DEFAULT_BOLD
        binding.changeDifficultyChip.typeface = Typeface.DEFAULT_BOLD
        binding.changeDifficultyChip.setOnClickListener {
            AlertDialog.Builder(activity)
                .setTitle("Change Difficulty")
                .setCancelable(true)
                .setItems(difficultiesReprs, DialogInterface.OnClickListener { dialog, choice ->
                    viewModel.changeDifficulty(Difficulty.valueOf(difficultiesReprs[choice]))
                })
                .show()
        }

        viewModel.pointsEarned.observe(viewLifecycleOwner, { score ->
            lifecycleScope.launch(Dispatchers.Main) {
                delay(1000L)
                val animTime = score / 2L
                launch {
                    animateScoreText(score, animTime)
                }
                launch {
                    playScoreSound(animTime)
                }
                launch {
                    delay(animTime)
                    animationFinished.value = true
                }
            }
        })

        animationFinished.observe(viewLifecycleOwner, { isFinished ->
            if (isFinished) {
                binding.newGameChip.setOnClickListener {//dont allow navigation until animation complete
                    findNavController().navigate(
                        GameSummaryFragmentDirections.actionGameSummaryToGame(
                            PlayRequest.NewGame(viewModel.nextGameDifficulty.value!!)
                        )
                    )
                }
            }
        })

        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
//        return layoutInflater.inflate(R.layout.fragment_game_summary, container, false)
    }

    private fun animateScoreText(score: Int, animTime: Long) {
        ValueAnimator.ofInt(0, score).apply {
            addUpdateListener {
                binding.gameSummaryPoints.text = String.format(
                    resources.getString(
                        R.string.points_earned_fmt,
                        it.animatedValue as Int
                    )
                )
            }
            duration = animTime
            start()
        }
    }

    private suspend fun playScoreSound(animTime: Long) {
        when (soundplayer) {
            is Some -> {
                val player = (soundplayer as Some<SoundPlayer>).t
                player.playLoop(SoundEffect.ButtonEventSound.ScoreClick)
                delay(animTime)
                player.pauseAll()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu_home, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (animationFinished.value ?: false)
            (NavigationUI.onNavDestinationSelected(item!!, requireView().findNavController())
                    || super.onOptionsItemSelected(item))
        else false
    }
}