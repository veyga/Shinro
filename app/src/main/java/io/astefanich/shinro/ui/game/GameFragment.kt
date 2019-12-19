package io.astefanich.shinro.ui.game


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.GameFragmentBinding

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        val gameFragmentArgs by navArgs<GameFragmentArgs>()

//        viewModelFactory = ViewModelFactory(gameFragmentArgs.boardId)
//        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
//
//        binding.nextArrow.setOnClickListener { view ->
//            view.findNavController()
//                .navigate(GameFragmentDirections.actionGameDestinationSelf(viewModel.board.value!!.boardNum + 1))
//        }
//        binding.previousArrow.setOnClickListener { view ->
//            view.findNavController()
//                .navigate(GameFragmentDirections.actionGameDestinationSelf(viewModel.board.value!!.boardNum - 1))
//        }
//        binding.vm = viewModel
//        binding.setLifecycleOwner(this)
//        setHasOptionsMenu(true)
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
        return NavigationUI.onNavDestinationSelected(item!!, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }
}
