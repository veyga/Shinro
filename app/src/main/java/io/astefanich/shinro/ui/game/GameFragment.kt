package io.astefanich.shinro.ui.game


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.GameFragmentBinding

/**
 * A simple [Fragment] subclass.
 */
class GameFragment : Fragment() {


    private lateinit var viewModel: GameViewModel
    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.game_fragment,container,false)
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }


}
