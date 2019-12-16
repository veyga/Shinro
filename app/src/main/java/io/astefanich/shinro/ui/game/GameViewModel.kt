package io.astefanich.shinro.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    private val score = 10
    private val _marblesRemaining = MutableLiveData<Int>()
    val marblesRemaining: LiveData<Int>
        get() = _marblesRemaining

    init {
        _marblesRemaining.value = 10
    }
}