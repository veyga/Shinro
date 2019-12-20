package io.astefanich.shinro.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.radutopor.viewmodelfactory.annotations.Provided
import io.astefanich.shinro.domain.Board
import io.astefanich.shinro.repository.BoardRepository
import javax.inject.Inject

class GameViewModel @Inject constructor(@Provided repository: BoardRepository, val boardId: Int) :
    ViewModel() {


    private val _board = MutableLiveData<Board>()
    val board: LiveData<Board>
        get() = _board

    init {
        _board.value = repository.getBoardById(boardId).value
    }


}