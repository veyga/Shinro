package io.astefanich.shinro.util

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import io.astefanich.shinro.util.sound.SoundEffect
import io.astefanich.shinro.viewmodels.*
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named


//sealed class VibrationEffect
class GameVibrator
@Inject
constructor(
    @Named("actCtx") val ctx: Context,
    val prefs: SharedPreferences,
) {

    private var BUZZ_AMPLITUDE = 50

    enum class BuzzDuration(val value: Long){
        SHORT(25),
        MEDIUM(250),
        LONG(500)
    }

    private var vibrationsEnabled = false

    private var buzzer: Vibrator? = ctx.getSystemService()

    val buzz =
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            { d: BuzzDuration -> buzzer?.vibrate(VibrationEffect.createOneShot(d.value, BUZZ_AMPLITUDE))}
        else
            { d: BuzzDuration -> buzzer?.vibrate(d.value)}

    init {
        vibrationsEnabled = prefs.getBoolean("vibrations_enabled", true)
        Timber.i("vibrationsEnabled? $vibrationsEnabled")
        var strengthPct = prefs.getInt("vibration_strength", 50)
        BUZZ_AMPLITUDE = Math.round(255 * (strengthPct / 100.0f))
        if(BUZZ_AMPLITUDE <= 0){
            BUZZ_AMPLITUDE = 1
        }
        Timber.i("strength is $BUZZ_AMPLITUDE")
    }

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