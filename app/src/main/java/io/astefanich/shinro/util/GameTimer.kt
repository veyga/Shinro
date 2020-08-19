package io.astefanich.shinro.util

import io.astefanich.shinro.common.TimePeriod
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timerTask

class GameTimer(val period: TimePeriod) {
    init {
        Timber.i("TIMER CREATED period is $period")
    }
    private var started = false
    private var isRunning = false
    private var timer: Timer = Timer()
    private var myTask = {}

    fun start(action: () -> Unit) {
        if (!started) {
                myTask = action
                Timber.i("starting timer")
                started = true
                isRunning = true
                Timber.i("its started and running")
                timer.scheduleAtFixedRate( timerTask { myTask() }, 0, period.seconds * 1000)
        } else{
            Timber.i("timer is already started. call resume instead")
        }
    }

    fun resume() {
        Timber.i("resume called")
        if (started && !isRunning){
            isRunning= true
            Timber.i("its now running")
            timer = Timer()
            timer.scheduleAtFixedRate( timerTask { myTask() }, 0, period.seconds * 1000)
        } else {
            Timber.i("Timer is not started. cant resume it")
        }
    }

    fun pause(){
        Timber.i("try to pausing")
        if(isRunning){
            timer.cancel()
            isRunning = false
            Timber.i("its no longer running; cancelled")
        } else {
            Timber.i("its not running. cant pause it")
        }
    }
}
