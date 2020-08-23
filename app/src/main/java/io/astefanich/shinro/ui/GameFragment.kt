package io.astefanich.shinro.ui


import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateUtils
import android.view.*
import androidx.core.view.get
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
import io.astefanich.shinro.common.Grid
import io.astefanich.shinro.databinding.FragmentGameBinding
import io.astefanich.shinro.util.ShinroTimer
import io.astefanich.shinro.util.GameSoundPlayer
import io.astefanich.shinro.viewmodels.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
import javax.inject.Inject


/**
 * Game UI controller
 */
class GameFragment : Fragment() {

    @Inject
    lateinit var bus: EventBus

    @Inject
    lateinit var uiTimer: Option<ShinroTimer>

    @Inject
    lateinit var soundEffectPlayer: GameSoundPlayer

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var toast: @JvmSuppressWildcards(true) (String) -> Unit

    @Inject
    lateinit var gameDialogBuilder: @JvmSuppressWildcards(true) (String, String, () -> Unit) -> AlertDialog.Builder

    private lateinit var viewModel: GameViewModel

    private lateinit var binding: FragmentGameBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity)
            .mainActivityComponent
            .getGameComponent()
            .inject(this)
        bus.register(this)
        bus.register(soundEffectPlayer)
//        requireActivity().onBackPressedDispatcher.addCallback(this) {
            //disable back button during active game. users can access home via home button
//            bus.post(SaveGameCommand)
//            val manager = (activity as MainActivity).supportFragmentManager
//            manager.popBackStackImmediate()
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        bus.post(SetOnClickListenersCommand)
        binding.lifecycleOwner = this@GameFragment
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameFragmentArgs by navArgs<GameFragmentArgs>()
        bus.post(LoadGameCommand(gameFragmentArgs.playRequest))
    }

    @Subscribe
    fun on(evt: GameLoadedEvent) {
        Timber.i("GameLoadedEvent")
        val drawable =
            if (evt.difficulty == Difficulty.EASY) R.drawable.ic_green_circle32
            else if (evt.difficulty == Difficulty.MEDIUM) R.drawable.ic_blue_square32
            else R.drawable.ic_gray_diamond32
        binding.apply {
            difficultyText.text = evt.difficulty.repr
            difficultyIcon.setImageDrawable(resources.getDrawable(drawable))
            freebiesRemaining.text = String.format(
                resources.getString(R.string.freebies_remaining_fmt),
                evt.freebiesRemaining
            )
            bus.post(RedrawGridCommand(evt.grid))
            progressBar.visibility = View.GONE
            game.visibility = View.VISIBLE
        }
        when (uiTimer) { //initialize UI timer
            is Some -> {
                var uiTime = evt.startTime
                val timer = (uiTimer as Some<ShinroTimer>)
                binding.timeElapsed.visibility = View.VISIBLE
                timer.t.start {
                    binding.timeElapsed.post {
                        binding.timeElapsed.text = String.format(
                            resources.getString(R.string.timer_fmt),
                            DateUtils.formatElapsedTime(uiTime)
                        )
                        uiTime += timer.t.period.seconds
                    }
                }
            }
        }
        bus.post(StartGameTimerCommand)
    }

    @Subscribe
    fun on(evt: MoveRecordedEvent) {
        ((binding.grid[evt.row] as ViewGroup)[evt.col] as SquareImageView).bindSvg(evt.newVal)
    }


    @Subscribe
    fun on(evt: TwelveMarblesPlacedEvent) {
        bus.post(CheckSolutionCommand)
    }

    @Subscribe
    fun on(evt: BoardResetEvent) {
        bus.post(RedrawGridCommand(evt.newBoard))
    }

    @Subscribe
    fun on(evt: RevertedToCheckpointEvent) {
        bus.post(RedrawGridCommand(evt.newBoard))
    }

    @Subscribe
    fun on(evt: UndoStackActivatedEvent) {
        binding.undoButton.apply {
            isClickable = true
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_undo_white, 0, 0)
            setTextColor(resources.getColor(R.color.white))
        }
    }

    @Subscribe
    fun on(evt: UndoStackDeactivatedEvent) {
        binding.undoButton.apply {
            isClickable = false
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_undo_gray, 0, 0)
            setTextColor(resources.getColor(R.color.lightGray))
        }
    }

    @Subscribe
    fun on(evt: CheckpointSetEvent) {
        binding.setCheckpoint.text = resources.getString(R.string.reset_checkpoint)
        binding.undoToCheckpoint.apply {
            isClickable = true
            setTextColor(resources.getColor(R.color.white))
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_flag_empty_white, 0, 0)
        }
    }

    @Subscribe
    fun on(evt: CheckpointResetEvent) {
        toast("Checkpoint Reset")
    }

    @Subscribe
    fun on(evt: CheckpointDeactivatedEvent) {
        binding.setCheckpoint.text = resources.getString(R.string.set_checkpoint)
        binding.undoToCheckpoint.apply {
            isClickable = false
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_flag_empty_gray, 0, 0)
            setTextColor(resources.getColor(R.color.lightGray))
        }
    }


    @Subscribe
    fun on(evt: CellUndoneEvent) {
        ((binding.grid[evt.row] as ViewGroup)[evt.col] as SquareImageView).bindSvg(evt.newVal)
    }

    @Subscribe
    fun on(evt: FreebiePlacedEvent) {
        val targetCell = (binding.grid[evt.row] as ViewGroup)[evt.col] as SquareImageView
        ObjectAnimator.ofArgb(
            targetCell,
            "backgroundColor",
            Color.RED,
            resources.getColor(R.color.darkRed)
        )
            .setDuration(3000)
            .start()
        targetCell.bindSvg("M")
    }


    @Subscribe
    fun on(evt: OutOfFreebiesEvent) {
        toast("Out of freebies")
    }

    @Subscribe
    fun on(evt: IncorrectSolutionEvent) {
        toast("${evt.numIncorrect} of your marbles are wrong")
    }

    @Subscribe
    fun on(evt: TooManyPlacedEvent) {
        toast("You have placed ${evt.numPlaced} marbles, which is too many")
    }

    @Subscribe
    fun on(evt: GameWonEvent) {
        toast("You Won!")
        bus.post(PauseGameTimerCommand)
        bus.post(TearDownGameCommand)
    }

    @Subscribe
    fun on(evt: GameLostEvent) {
        bus.post(PauseGameTimerCommand)
        bus.post(TearDownGameCommand)
    }

    @Subscribe
    fun on(evt: GameTornDownEvent) {
        var navDelay = if (evt.summary.isWin) 2000L else 1000L
        when (uiTimer) {
            is Some -> (uiTimer as Some<ShinroTimer>).t.pause()
        }
        lifecycleScope.launch(Dispatchers.IO) {
            delay(navDelay)
            withContext(Dispatchers.Main) {
                findNavController().navigate(GameFragmentDirections.actionGameToGameSummary(evt.summary))
            }
        }

    }

    @Subscribe
    fun handle(cmd: SetOnClickListenersCommand) {
        for (i in 1..8) {
            val row = binding.grid[i] as ViewGroup
            for (j in 1..8) {
                val cell = row[j]
                cell.setOnClickListener { bus.post(MoveCommand(i, j)) }
            }
        }
        binding.apply {
            surrenderBoard.setOnClickListener {
                gameDialogBuilder(
                    "Surrender",
                    "Are you sure?"
                ) { bus.post(SurrenderCommand) }.show()
            }
            resetBoard.setOnClickListener {
                gameDialogBuilder(
                    "Reset",
                    "Clear the board?\n(freebie will persist if used)"
                ) { bus.post(ResetBoardCommand) }.show()
            }
            freebiesRemaining.setOnClickListener {
                gameDialogBuilder(
                    "Freebie",
                    "Use freebie?\nThis will persist until the game is over"
                ) { bus.post(UseFreebieCommand) }.show()
            }
            setCheckpoint.setOnClickListener { bus.post(SetCheckpointCommand) }
            undoToCheckpoint.setOnClickListener { bus.post(UndoToCheckpointCommand) }
            undoButton.setOnClickListener { bus.post(UndoCommand) }
            solveButton.setOnClickListener { bus.post(SolveBoardCommand) }
        }
    }

    @Subscribe
    fun handle(cmd: RedrawGridCommand) {
        for (i in 0..8) {
            val row = binding.grid[i] as ViewGroup
            for (j in 0..8) {
                val cell = row[j] as SquareImageView
                cell.bindSvg(cmd.newBoard[i][j].current)
            }
        }
    }

    override fun onStart() {
        Timber.i("onStart")
        super.onStart()
        bus.post(ResumeGameTimerCommand)
        when (uiTimer) {
            is Some -> (uiTimer as Some<ShinroTimer>).t.resume()
        }
    }

    override fun onStop() {
        Timber.i("onStop")
        bus.post(PauseGameTimerCommand)
        bus.post(SaveGameCommand) //leave this here. not in onDestroy
        when (uiTimer) {
            is Some -> (uiTimer as Some<ShinroTimer>).t.pause()
        }
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
        if (bus.isRegistered(this))
            bus.unregister(this)
        if(bus.isRegistered(soundEffectPlayer))
            bus.unregister(soundEffectPlayer)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.settings_destination -> {
//                findNavController().navigate(GameFragmentDirections.actionGameToSettings())
//                true
//            }
//            else -> (NavigationUI.onNavDestinationSelected( item!!, requireView().findNavController() )
//                    || super.onOptionsItemSelected(item))
//        }
        Timber.i("you selected an option")
        return (NavigationUI.onNavDestinationSelected(item!!, requireView().findNavController())
                || super.onOptionsItemSelected(item))
    }


    data class RedrawGridCommand(val newBoard: Grid)
    object SetOnClickListenersCommand
}
