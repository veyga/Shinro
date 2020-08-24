package io.astefanich.shinro.util

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import io.astefanich.shinro.common.*
import org.greenrobot.eventbus.Subscribe

class GameVibrator(
    private val vibrator: Vibrator,
    private val amplitude: Int
) {
    enum class BuzzDuration(val value: Long){
        SHORT(25),
        MEDIUM(250),
        LONG(500)
    }

    private val buzz =
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            { d: BuzzDuration -> vibrator.vibrate(VibrationEffect.createOneShot(d.value, amplitude))}
        else
            { d: BuzzDuration -> vibrator.vibrate(d.value)}

    @Subscribe
    fun on(evt: MoveRecordedEvent) {
        buzz(BuzzDuration.SHORT)
    }

    @Subscribe
    fun on(evt: CellUndoneEvent) {
        buzz(BuzzDuration.SHORT)
    }

    @Subscribe
    fun on(evt: RevertedToCheckpointEvent) {
        buzz(BuzzDuration.MEDIUM)
    }

    @Subscribe
    fun on(evt: BoardResetEvent) {
        buzz(BuzzDuration.MEDIUM)
    }

    @Subscribe
    fun on(evt: CheckpointSetEvent) {
        buzz(BuzzDuration.SHORT)
    }

    @Subscribe
    fun on(evt: CheckpointResetEvent) {
        buzz(BuzzDuration.MEDIUM)
    }

    @Subscribe
    fun on(evt: GameWonEvent) {
        buzz(BuzzDuration.LONG)
    }

    @Subscribe
    fun on(evt: GameLostEvent) {
        buzz(BuzzDuration.LONG)
    }

    @Subscribe
    fun on(evt: FreebiePlacedEvent) {
        buzz(BuzzDuration.SHORT)
    }
}