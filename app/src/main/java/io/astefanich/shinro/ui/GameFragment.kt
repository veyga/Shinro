package io.astefanich.shinro.ui


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.FragmentGameBinding
import io.astefanich.shinro.di.DaggerAppComponent
import io.astefanich.shinro.di.game.GameComponent
import io.astefanich.shinro.di.game.GameModule
import io.astefanich.shinro.domain.BoardCount
import io.astefanich.shinro.viewmodels.GameViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class GameFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var boardCount: BoardCount

    @Inject
    lateinit var winBuzzPattern: LongArray

//    @Inject
//    @field:Named("winBuzz")
//    lateinit var winBuzzPattern: LongArray
//
//    @Inject
//    @field:Named("resetBuzz")
//    lateinit var resetBuzzPattern: LongArray



    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGameBinding
    private lateinit var gameComponent: GameComponent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        val gameFragmentArgs by navArgs<GameFragmentArgs>()
        var boardId = gameFragmentArgs.boardId



        gameComponent = DaggerAppComponent
            .builder()
            .application(activity!!.application)
            .build()
            .getGameComponentBuilder()
            .gameModule(GameModule(boardId))
            .build()
        gameComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)

        if (viewModel.boardId == 1)
            binding.backArrow.visibility = View.INVISIBLE

        if (viewModel.boardId == boardCount.value)
            binding.nextArrow.visibility = View.INVISIBLE

        viewModel.gameWon.observe(this, Observer { isWon ->
            if (isWon) {
                buzz(winBuzzPattern)
            }
        })

        binding.nextArrow.setOnClickListener { view ->
            view.findNavController()
                .navigate(
                    GameFragmentDirections.actionGameDestinationSelf(
                        viewModel.boardId + 1
                    )
                )
        }
        binding.backArrow.setOnClickListener { view ->
            view.findNavController()
                .navigate(
                    GameFragmentDirections.actionGameDestinationSelf(
                        viewModel.boardId - 1
                    )
                )
        }

        binding.resetBoard.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(activity)
            dialogBuilder
                .setTitle("Reset Board")
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("YES", DialogInterface.OnClickListener { dialog, id ->
                    buzz(winBuzzPattern) //TODO resetBuzzPattern
                    viewModel.onReset()
                })
                .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, id ->
                })
                .create()
                .show()
        }

        binding.vm = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }


    //onDestroy isn't reliably called. This call reliably saves last visited board
    override fun onStop() {
        super.onStop()
        viewModel.saveLastVisited()
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }
}
