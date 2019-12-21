package io.astefanich.shinro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import com.radutopor.viewmodelfactory.annotations.Provided
import com.radutopor.viewmodelfactory.annotations.ViewModelFactory
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.repository.BoardRepository
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@ViewModelFactory
class GameViewModel @Inject constructor(@Provided val repository: BoardRepository, val boardId: Int) :
    ViewModel() {

    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board>
        get() = _board


    init {
        Timber.i("viewmodel created")
        _board.value = repository.getBoardById(boardId).value
        Timber.i("gameviewmodel got this from the repositoy for id: $boardId \n ${_board.value}")
    }

    fun onMove() {
//        _board.value!!.marblesPlaced = (_board.value!!.marblesPlaced) - 1
        Timber.i("MOVE PRESSED. marbles placed is now ${_board.value?.marblesPlaced}")
    }

}