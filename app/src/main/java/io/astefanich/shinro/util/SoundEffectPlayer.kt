package io.astefanich.shinro.util

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
import io.astefanich.shinro.R
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.viewmodels.*
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@PerFragment
class SoundEffectPlayer
@Inject
constructor(
    val prefs: SharedPreferences,
    @Named("actCtx") ctx: Context
) {

    lateinit var ctx: Context
    private lateinit var soundPool: SoundPool
    private var clicksEnabled = false
    private var buttonsEventsEnabled = false
    private var checkpointSetResetSound = 0
    private var cellClickSound = 0
    private var freebieSound = 0
    private var gameWonSound = 0
    private var boardResetSound = 0
    private var gameLostSound = 0
    private var undoneSound = 0
    private var checkpointRevertedSound = 0

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(8)
            .build()
        clicksEnabled = prefs.getBoolean("click_sound_enabled", false)
        buttonsEventsEnabled = prefs.getBoolean("buttons_events_sound_enabled", false)
        if (clicksEnabled)
            cellClickSound = soundPool.load(ctx, R.raw.click, 1)
        if (buttonsEventsEnabled) {
            checkpointSetResetSound = soundPool.load(ctx, R.raw.checkpoint_set, 1)
            freebieSound = soundPool.load(ctx, R.raw.freebie, 1)
            gameWonSound = soundPool.load(ctx, R.raw.game_won, 1)
            boardResetSound = soundPool.load(ctx, R.raw.reset, 1)
            gameLostSound = soundPool.load(ctx, R.raw.surrender, 1)
            undoneSound = soundPool.load(ctx, R.raw.undo, 1)
            checkpointRevertedSound = soundPool.load(ctx, R.raw.undo_checkpoint, 1)
        }

    }

    @Subscribe
    fun on(evt: MoveRecordedEvent) {
        if (clicksEnabled)
            soundPool.play(cellClickSound, 1f, 1f, 0, 0, 1f)
    }

    @Subscribe
    fun on(evt: CellUndoneEvent) {
        if (buttonsEventsEnabled)
            soundPool.play(undoneSound, 1f, 1f, 0, 0, 1f)
    }

    @Subscribe
    fun on(evt: RevertedToCheckpointEvent) {
        if (buttonsEventsEnabled)
            soundPool.play(checkpointRevertedSound, 1f, 1f, 0, 0, 1f)
    }

    @Subscribe
    fun on(evt: BoardResetEvent) {
        if (buttonsEventsEnabled)
            soundPool.play(boardResetSound, 1f, 1f, 0, 0, 1f)
    }

    @Subscribe
    fun on(evt: CheckpointSetEvent) {
        if (buttonsEventsEnabled)
            soundPool.play(checkpointSetResetSound, 1f, 1f, 0, 0, 1f)
    }

    @Subscribe
    fun on(evt: CheckpointResetEvent) {
        if (buttonsEventsEnabled)
            soundPool.play(checkpointSetResetSound, 1f, 1f, 0, 0, 1f)
    }

    @Subscribe
    fun on(evt: GameWonEvent) {
        if (buttonsEventsEnabled)
            soundPool.play(gameWonSound, 1f, 1f, 0, 0, 1f)
    }

    @Subscribe
    fun on(evt: GameLostEvent) {
        if (buttonsEventsEnabled)
            soundPool.play(gameLostSound, 1f, 1f, 0, 0, 1f)
    }

    @Subscribe
    fun on(evt: FreebiePlacedEvent) {
        if (buttonsEventsEnabled)
            soundPool.play(freebieSound, 1f, 1f, 0, 0, 1f)
    }
}