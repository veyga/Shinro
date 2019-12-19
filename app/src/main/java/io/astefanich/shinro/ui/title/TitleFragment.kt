package io.astefanich.shinro.ui.title


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.GameFragmentBinding
import io.astefanich.shinro.databinding.TitleFragmentBinding
import io.astefanich.shinro.ui.game.GameViewModel
import io.astefanich.shinro.ui.game.TitleViewModel
import io.astefanich.shinro.ui.game.ViewModelFactory
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: TitleViewModel
    private lateinit var binding: TitleFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidSupportInjection.inject(this)

        val binding: TitleFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.title_fragment, container, false)


        viewModel = ViewModelProviders.of(this,viewModelFactory).get(TitleViewModel::class.java)

        binding.vm = viewModel
        binding.setLifecycleOwner(this)

//        binding.playButton.setOnClickListener {
//            findNavController().navigate(TitleFragmentDirections.actionTitleToGame(0))
//        }
//        binding.howToPlayButton.setOnClickListener {
//            findNavController().navigate(TitleFragmentDirections.actionTitleToRules())
//        }
//        binding.aboutButton.setOnClickListener {
//            findNavController().navigate(TitleFragmentDirections.actionTitleToAbout())
//        }

        return binding.root
    }


}
