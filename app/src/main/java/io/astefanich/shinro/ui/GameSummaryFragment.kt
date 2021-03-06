package io.astefanich.shinro.ui

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
    lateinit var soundPlayer: Option<SoundPlayer>

    private lateinit var viewModel: GameSummaryViewModel

    private var _binding: FragmentGameSummaryBinding? = null
    private val binding get() = _binding!!

    private var animationComplete = false
    private var publishingComplete = false

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

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_summary, container, false)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameSummaryViewModel::class.java)
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
                    animationComplete = true
                }
            }
        })

        viewModel.scoresPublished.observe(viewLifecycleOwner, { isSaved ->
            if (!isSaved) {
                AlertDialog.Builder(requireActivity())
                    .setMessage(R.string.publishing_failed)
                    .setNeutralButton(android.R.string.ok, null).show()
            }
            publishingComplete = true
            binding.newGameChip.setOnClickListener {
                if (animationComplete && publishingComplete)
                    findNavController().navigate(
                        GameSummaryFragmentDirections.actionGameSummaryToGame(
                            PlayRequest.NewGame(viewModel.nextGameDifficulty.value!!)
                        )
                    )
            }
        })

        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
//        return layoutInflater.inflate(R.layout.fragment_game_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (animationComplete && publishingComplete)
                findNavController().navigate(
                    GameSummaryFragmentDirections.actionGameSummaryToGame(
                        PlayRequest.NewGame(viewModel.nextGameDifficulty.value!!)
                    )
                )
        }
    }

    override fun onStart() {
        super.onStart()
        binding.newGameChip.setTextColor(resources.getColor(R.color.white))
        binding.changeDifficultyChip.setTextColor(resources.getColor(R.color.white))
        binding.newGameChip.typeface = Typeface.DEFAULT_BOLD
        binding.changeDifficultyChip.typeface = Typeface.DEFAULT_BOLD
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
        when (soundPlayer) {
            is Some -> {
                val player = (soundPlayer as Some<SoundPlayer>).t
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
        return if (animationComplete && publishingComplete)
            (NavigationUI.onNavDestinationSelected(item!!, requireView().findNavController())
                    || super.onOptionsItemSelected(item))
        else false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
