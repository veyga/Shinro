package io.astefanich.shinro.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class GameViewModelFactory(private val boardId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(boardId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}