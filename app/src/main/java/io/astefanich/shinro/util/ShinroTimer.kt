package io.astefanich.shinro.util

import android.os.SystemClock
import android.text.format.DateUtils
import android.widget.Chronometer
import io.astefanich.shinro.common.TimeSeconds
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timerTask

class ShinroTimer(val period: TimeSeconds = TimeSeconds.ONE) {
    private var isStarted = false
    private var isRunning = false
    private var timer: Timer = Timer()
    private var myTask = {}

    fun start(action: () -> Unit) {
        if (!isStarted) {
            myTask = action
            isStarted = true
            isRunning = true
            timer.scheduleAtFixedRate(timerTask { myTask() }, 0, period.seconds * 1000)
        } else{
            Timber.i("timer already running")
        }
    }

    fun resume() {
        if (isStarted && !isRunning) {
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


class UITimer (
    val period: TimeSeconds,
    val base: Long, //initial gametime
    val chronometer: Chronometer
) {
    private var isStarted = false
    private var isRunning = false
    private var offset = 0L

    init {
        chronometer.base = SystemClock.elapsedRealtime() - base
        chronometer.setOnChronometerTickListener {
            if ((SystemClock.elapsedRealtime() - chronometer.base) % period.seconds == 0L)
                chronometer.text = "Time:\n${DateUtils.formatElapsedTime((SystemClock.elapsedRealtime() - chronometer.base) / 1000L)}"

        }
    }


    fun start() {
        Timber.i("starting ui timer")
        if (!isStarted) {
            isStarted = true
            isRunning = true
            chronometer.base = SystemClock.elapsedRealtime() - offset
            chronometer.start()
        }
    }

    fun resume() {
        if(isStarted && !isRunning){
            chronometer.base = SystemClock.elapsedRealtime() - offset
            chronometer.start()
            isRunning = true
        }
    }

    fun pause() {
        if (isRunning) {
            Timber.i("pausing ui timer")
            chronometer.stop()
            offset = SystemClock.elapsedRealtime() - chronometer.base
            Timber.i("ui timer paused at ${DateUtils.formatElapsedTime((SystemClock.elapsedRealtime() - chronometer.base) / 1000L)}")
            isRunning = false
        }
    }
}
