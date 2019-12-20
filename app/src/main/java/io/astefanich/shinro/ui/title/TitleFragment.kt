package io.astefanich.shinro.ui.title


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.TitleFragmentBinding
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.repository.BoardRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class TitleFragment : Fragment() {


    @Inject
    lateinit var repository: BoardRepository

    @Inject
    lateinit var sampleData: Array<Board>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidSupportInjection.inject(this)

        val binding: TitleFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.title_fragment, container, false)


        binding.playButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToGame(0))
        }
        binding.howToPlayButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToRules())
        }
        binding.aboutButton.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToAbout())
        }

        binding.loadBoardsButton.setOnClickListener {
//            Timber.i("fragment trying to get board")
//            repository.getBoardById(1)
//            Timber.i("fragement requesting to load boards")
//            repository.insertBoards(*sampleData)

            Timber.i("fragment inserting 1 board")
//            repository.insertOneBoard(Board(5,"MEDIUM"))
            Timber.i("fragment calling get all boards on repo")
            repository.getAllBoards()
        }
        return binding.root
    }


}
