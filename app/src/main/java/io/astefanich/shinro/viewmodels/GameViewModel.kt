package io.astefanich.shinro.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.radutopor.viewmodelfactory.annotations.Provided
import com.radutopor.viewmodelfactory.annotations.ViewModelFactory
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.repository.BoardRepository
import timber.log.Timber
import javax.inject.Inject

@ViewModelFactory
class GameViewModel @Inject constructor(@Provided val repository: BoardRepository, val boardId: Int) :
    ViewModel() {


    //instance for game logic
    private val _board: Board

    //observable type for board
    val board = MutableLiveData<Board>()


    init {
        _board = repository.getBoardById(boardId)
//        board.observeForever()
        board.value = _board
    }

    fun onMove() {
        Timber.i("Moving. marbled placed increase by one")
        _board.marblesPlaced += 1
        board.value = _board
    }

}