package io.astefanich.shinro.ui.title


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import dagger.android.DaggerFragment

import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.TitleFragmentBinding
import io.astefanich.shinro.repository.BoardRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment() {

//    @Inject
//    lateinit var repository: BoardRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Toast.makeText(activity, "title fragment", Toast.LENGTH_SHORT).show()

        val binding: TitleFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.title_fragment, container, false)

//        binding.playButton.setOnClickListener {
//            findNavController().navigate(TitleFragmentDirections.actionTitleToGame(0))
//        }
//        binding.howToPlayButton.setOnClickListener {
//            findNavController().navigate(TitleFragmentDirections.actionTitleToRules())
//        }
//        binding.aboutButton.setOnClickListener {
//            findNavController().navigate(TitleFragmentDirections.actionTitleToAbout())
//        }


//        Timber.i("repo is null? ${repository == null}")
        return binding.root
    }


}
