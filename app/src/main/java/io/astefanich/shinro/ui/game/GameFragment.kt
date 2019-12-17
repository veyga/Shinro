package io.astefanich.shinro.ui.game


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs

import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.GameFragmentBinding

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {


    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        val gameFragmentArgs by navArgs<GameFragmentArgs>()

        viewModelFactory = GameViewModelFactory(gameFragmentArgs.boardId)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)

        binding.nextArrow.setOnClickListener { view ->
            view.findNavController()
                .navigate(GameFragmentDirections.actionGameDestinationSelf(viewModel.board.value!!.boardNum + 1))
        }
        binding.vm = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }


}
