package io.astefanich.shinro.util

import io.astefanich.shinro.common.TimeSeconds
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timerTask

class ShinroTimer(val period: TimeSeconds) {
    private var started = false
    private var isRunning = false
    private var timer: Timer = Timer()
    private var myTask = {}

    fun start(action: () -> Unit) {
        if (!started) {
            myTask = action
            started = true
            isRunning = true
            timer.scheduleAtFixedRate(timerTask { myTask() }, 0, period.seconds * 1000)
        }
    }

    fun resume() {
        if (started && !isRunning) {
            isRunning = true
            timer = Timer()
            timer.scheduleAtFixedRate(timerTask { myTask() }, 0, period.seconds * 1000)
        }
    }

    fun pause() {
        if (isRunning) {
            timer.cancel()
            isRunning = false
        }
    }
}
