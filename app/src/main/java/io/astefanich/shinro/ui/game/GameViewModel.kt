package io.astefanich.shinro.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private var repoJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + repoJob)

//    private val _board = MutableLiveData<Board>()
//    val board: LiveData<Board>
//        get() = _board


    private val boards = repository.getAllBoards()
    private val currentBoard = MutableLiveData<Board>()

    init {
        initializeCurrentBoard()
        Timber.i("repo got all boards, size= ${boards.value}")
//        Timber.i("viewmodel created")
//        _board.value = repository.getBoardById(boardId).value
//        Timber.i("gameviewmodel got this from the repositoy for id: $boardId \n ${_board.value}")
    }

    private fun initializeCurrentBoard() {
        uiScope.launch {
            currentBoard.value = getCurrentBoardFromDatabase()
        }
    }


    private suspend fun getCurrentBoardFromDatabase(): Board? {
        return withContext(Dispatchers.IO) {
            var board = repository.getBoardById(1)
            board.value
        }
    }
}