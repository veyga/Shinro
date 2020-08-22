package io.astefanich.shinro.util

import io.astefanich.shinro.common.TimeSeconds
import java.util.*
import kotlin.concurrent.timerTask

class ShinroTimer(val period: TimeSeconds = TimeSeconds.ONE) {
    val isStarted: Boolean
        get() = _isRunning
    val isRunning: Boolean
        get() = _isRunning
    private var _isStarted = false
    private var _isRunning = false
    private var timer: Timer = Timer()
    private var myTask = {}

    fun start(action: () -> Unit) {
        if (!_isStarted) {
            myTask = action
            _isStarted = true
            _isRunning = true
            timer.scheduleAtFixedRate(timerTask { myTask() }, 0, period.seconds * 1000)
        }
    }

    fun resume() {
        if (_isStarted && !_isRunning) {
            _isRunning = true
            timer = Timer()
            timer.scheduleAtFixedRate(timerTask { myTask() }, 0, period.seconds * 1000)
        }
    }

    fun pause() {
        if (_isStarted && _isRunning) {
            timer.cancel()
            _isRunning = false
        }
    }
}


