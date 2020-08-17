package io.astefanich.shinro.ui


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import io.astefanich.shinro.R
import io.astefanich.shinro.ShinroApplication
import io.astefanich.shinro.databinding.FragmentGameBinding
import io.astefanich.shinro.viewmodels.GameViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class GameFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    @field:Named("winBuzz")
    lateinit var winBuzzPattern: LongArray

    @Inject
    @field:Named("resetBuzz")
    lateinit var resetBuzzPattern: LongArray

    @Inject
    lateinit var ctx: Context

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGameBinding
    private val toast = { msg: String -> Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        val gameFragmentArgs by navArgs<GameFragmentArgs>()
        var playRequest = gameFragmentArgs.playRequest

        (activity!!.application as ShinroApplication)
            .appComponent
            .getGameComponentBuilder()
            .playRequest(playRequest)
            .build()
            .inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)


        viewModel.gameWonBuzz.observe(viewLifecycleOwner, Observer { isWon ->
            if (isWon) {
                buzz(winBuzzPattern)
            }
        })


        viewModel.toastMe.observe(viewLifecycleOwner, Observer { toast(it) })

//        binding.nextArrow.setOnClickListener { view ->
//            view.findNavController()
//                .navigate(
//                    GameFragmentDirections.actionGameToGameSummary(viewModel.getSummary())
//                )
//        }

        binding.resetBoard.setOnClickListener {
            AlertDialog.Builder(activity)
                .setTitle("Reset Board")
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("YES", DialogInterface.OnClickListener { dialog, id ->
                    buzz(resetBuzzPattern)
                    viewModel.onReset()
                })
                .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, id ->
                })
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
        viewModel.saveGame()
    }

    private fun buzz(pattern: LongArray) {
//        VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE)
            Timber.i("buzzzing")
//                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            Timber.i("cant buzz yo")
//                buzzer.vibrate(pattern, -1) //deprecated in API 26
        }
//        val buzzer = activity?.getSystemService<Vibrator>()
//        buzzer?.let {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
//            } else {
//                buzzer.vibrate(pattern, -1) //deprecated in API 26
//            }
//        }
    }
}
