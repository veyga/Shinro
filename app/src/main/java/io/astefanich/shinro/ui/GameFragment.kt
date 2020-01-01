package io.astefanich.shinro.ui


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import dagger.android.support.AndroidSupportInjection
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.GameFragmentBinding
import io.astefanich.shinro.viewmodels.GameViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidSupportInjection.inject(this)

        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        val gameFragmentArgs by navArgs<GameFragmentArgs>()
        val boardId = gameFragmentArgs.boardId
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        viewModel.boardId = boardId
        viewModel.load()

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
                    viewModel.onReset()
                })
                .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, id ->

                })
                .create()
                .show()
        }


        viewModel.onLastBoard.observe(this, Observer { onLastBoard ->
            if (onLastBoard) {
                findNavController().navigate(GameFragmentDirections.actionGameToCheckBack())
            }
        })
        binding.vm = viewModel
        binding.setLifecycleOwner(this)
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("GameFragment", "destroyed")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //TODO need to use transaction manager so options pop back to game
        fragmentManager?.beginTransaction()?.addToBackStack("gameFragment")
        return NavigationUI.onNavDestinationSelected(item!!, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

}
