package io.astefanich.shinro.ui


import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.astefanich.shinro.R
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.Grid
import io.astefanich.shinro.common.PlayRequest
import io.astefanich.shinro.databinding.FragmentGameBinding
import io.astefanich.shinro.util.bindGridSvg
import io.astefanich.shinro.viewmodels.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named


class GameFragment : Fragment() {

    init {
        Timber.i("Game Fragment Init")
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var toast: @JvmSuppressWildcards(true) (String) -> Unit

    @Inject
    lateinit var gameDialogBuilder: @JvmSuppressWildcards(true) (String, String, () -> Unit) -> AlertDialog.Builder


//    @Inject
//    lateinit var bus: EventBus
    @Inject
    lateinit var nums: List<Int>

    @Inject
    @field:Named("actCtx")
    lateinit var actCtx: Context

    @Inject
    @field:Named("appCtx")
    lateinit var appCtx: Context

    private var bus = EventBus.getDefault()
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        AndroidSupportInjection.inject(this)

        val gameFragmentArgs by navArgs<GameFragmentArgs>()
        var playRequest = gameFragmentArgs.playRequest
        bus.post(LoadGameCommand(playRequest))
//        (activity as MainActivity)
//            .getMainActivityComponent()
//            .getGameComponentBuilder()
//            .playRequest(playRequest)
//            .build()
//            .inject(this)
        Timber.i("Game Fragment onCreateView")
        Timber.i("I got the nums: $nums")
        Timber.i("appCtx: $appCtx")
        Timber.i("actCtx: $actCtx")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        for (i in 1..8) {
            val row = binding.grid[i] as ViewGroup
            for (j in 1..8) {
                val cell = row[j]
                cell.setOnClickListener { bus.post(MoveCommand(i, j)) }
            }
        }
        binding.surrenderBoard.setOnClickListener {
            gameDialogBuilder("Surrender", "Are you sure?") { bus.post(SurrenderCommand) }.show() }
        binding.resetBoard.setOnClickListener {
            gameDialogBuilder("Reset", "Clear the board?\n(freebie will persist if used)") { bus.post(ResetBoardCommand) }.show() }
        binding.freebiesRemaining.setOnClickListener {
            gameDialogBuilder("Freebie", "Use freebie?\nThis will persist until the game is over") { bus.post(UseFreebieCommand) }.show() }
        binding.setCheckpoint.setOnClickListener { bus.post(SetCheckpointCommand) }
        binding.undoToCheckpoint.setOnClickListener { bus.post(UndoToCheckpointCommand) }
        binding.undoButton.setOnClickListener { bus.post(UndoCommand) }
        binding.solveButton.setOnClickListener { bus.post(SolveBoardCommand)}
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.i("game fragment onViewCreated")
//        super.onViewCreated(view, savedInstanceState)
//        val gameFragmentArgs by navArgs<GameFragmentArgs>()
//        var playRequest = gameFragmentArgs.playRequest
//        bus.post(LoadGameCommand(playRequest))
    }

    //onDestroy isn't reliably called. This call reliably saves active game
    override fun onStop() {
        super.onStop()
        bus.unregister(this)
        Timber.i("ON STOP")
        bus.post(PauseTimerCommand)
        bus.post(SaveGameCommand)
    }

    override fun onStart() {
        super.onStart()
        Timber.i("ON START")
        bus.register(this)
        bus.post(ResumeTimerCommand)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("ON DESTROY")
    }

    //Associating views with command objects (issuing Command objects not possible via XML)
    private fun initListeners() {
    }


    @Subscribe
    fun on(evt: GameLoadedEvent) {
        binding.difficultyText.text = evt.difficulty.repr
        val drawable =
            if (evt.difficulty == Difficulty.EASY) R.drawable.ic_green_circle32
            else if (evt.difficulty == Difficulty.MEDIUM) R.drawable.ic_blue_square32
            else R.drawable.ic_gray_diamond32
        binding.difficultyIcon.setImageDrawable(resources.getDrawable(drawable))
        binding.freebiesRemaining.text = "Freebies Remaining:\n${evt.freebiesRemaining} / 1"
        binding.timeElapsed.text = String.format(resources.getString(R.string.timer_fmt), DateUtils.formatElapsedTime(evt.elapsedTime))
        refreshGrid(evt.grid)
        binding.progressBar.visibility = View.GONE
        binding.game.visibility = View.VISIBLE
    }

    @Subscribe
    fun on(evt: TimeIncrementedEvent) {
//            if (timerVisible && (evt.sec % uiTimePeriod.seconds) == 0L)
        binding.timeElapsed.text = String.format( resources.getString(R.string.timer_fmt), DateUtils.formatElapsedTime(evt.sec) )
    }

    @Subscribe
    fun on(evt: CellChangedEvent) {
        ((binding.grid[evt.row] as ViewGroup)[evt.col] as SquareImageView).bindSvg(evt.newVal)
    }

    @Subscribe
    fun on(evt: TwelveMarblesPlacedEvent) {
        bus.post(CheckSolutionCommand)
    }

    @Subscribe
    fun on(evt: BoardResetEvent) {
        refreshGrid(evt.newBoard)
        toast("Cleared")
    }

    @Subscribe
    fun on(evt: RevertedToCheckpointEvent){
        refreshGrid(evt.newBoard)
        toast("Reverted")
    }

    @Subscribe
    fun on(evt: UndoStackActivatedEvent) {
        binding.undoButton.isClickable = true
        binding.undoToCheckpoint.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.ic_undo_white, 0,0)
        binding.undoToCheckpoint.setTextColor(resources.getColor(R.color.white))
    }

    @Subscribe
    fun on(evt: UndoStackDeactivatedEvent) {
        binding.undoButton.isClickable = false
        binding.undoToCheckpoint.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.ic_undo_gray, 0,0)
        binding.undoToCheckpoint.setTextColor(resources.getColor(R.color.lightGray))
    }

    @Subscribe
    fun on(evt: CheckpointSetEvent) {
        binding.undoToCheckpoint.isClickable = true
        binding.setCheckpoint.text = resources.getString(R.string.reset_checkpoint)
        binding.undoToCheckpoint.setTextColor(resources.getColor(R.color.white))
        binding.undoToCheckpoint.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.ic_flag_empty_white, 0,0)
    }

    @Subscribe
    fun on(evt: CheckpointResetEvent) {
        toast("Checkpoint Reset")
    }

    @Subscribe
    fun on(evt: CheckpointDeactivatedEvent) {
        binding.undoToCheckpoint.isClickable = false
        binding.undoToCheckpoint.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.ic_flag_empty_gray, 0,0)
        binding.undoToCheckpoint.setTextColor(resources.getColor(R.color.lightGray))
    }


    @Subscribe
    fun on(evt: FreebiePlacedEvent) {
        val targetCell = (binding.grid[evt.row] as ViewGroup)[evt.col] as SquareImageView
            ObjectAnimator.ofArgb(
                targetCell,
                "backgroundColor", Color.RED, resources.getColor(R.color.darkRed))
                .setDuration(3000)
                .start()
        bindGridSvg(targetCell, "M")
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
    fun on(evt: GameCompletedEvent) {
        bus.post(PauseTimerCommand)
        var navDelay = 500L
        if (evt.summary.isWin) {
                toast("You Won!")
                navDelay = 1500L
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(navDelay)
            findNavController().navigate(GameFragmentDirections.actionGameToGameSummary(evt.summary))
        }
    }

    @Subscribe
    fun on(evt: BoardSolvedEvent) {
        bus.post(CheckSolutionCommand) //its solved. but this triggers the remaining teardown cmds/events
    }

    private fun refreshGrid(grid: Grid){
        for (i in 1..8) {
            val row = binding.grid[i] as ViewGroup
            for (j in 1..8) {
                val cell = row[j] as SquareImageView
                cell.bindSvg(grid[i][j].current)
            }
        }
    }
}

//    @Inject
//    lateinit var sharedPreferences: SharedPreferences

//    private lateinit var uiTimePeriod: TimeSeconds
//private var timerVisible = false

//        setHasOptionsMenu(true)
//
//        if (sharedPreferences.getBoolean("timer_visible", true)) {
//            timerVisible = true
//            binding.timeElapsed.visibility = View.VISIBLE
//            uiTimePeriod = when (sharedPreferences.getString("timer_increment", "")) {
//                "5 seconds" -> TimeSeconds.FIVE
//                "10 seconds" -> TimeSeconds.TEN
//                "30 seconds" -> TimeSeconds.THIRTY
//                else -> TimeSeconds.ONE
//            }
//        }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater?.inflate(R.menu.overflow_menu, menu)
//    }
//
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.settings_destination -> {
//                findNavController().navigate(GameFragmentDirections.actionGameToSettings())
//                true
//            }
//            else -> {
//                (NavigationUI.onNavDestinationSelected(item!!, requireView().findNavController())
//                        || super.onOptionsItemSelected(item))
//            }
//        }
//    }
