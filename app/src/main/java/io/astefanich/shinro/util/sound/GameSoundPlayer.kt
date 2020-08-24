package io.astefanich.shinro.util.sound

import android.content.Context
import io.astefanich.shinro.common.*
import org.greenrobot.eventbus.Subscribe


class GameSoundPlayer(
    ctx: Context,
    clicksEnabled: Boolean,
    buttonsEventsEnabled: Boolean
) : AbstractSoundPlayer(ctx, clicksEnabled, buttonsEventsEnabled, 8) {


    init {
        loadSound(SoundEffect.CellClick)
        loadSound(SoundEffect.ButtonEventSound.CheckpointSet)
        loadSound(SoundEffect.ButtonEventSound.FreebiePlaced)
        loadSound(SoundEffect.ButtonEventSound.GameWon)
        loadSound(SoundEffect.ButtonEventSound.BoardReset)
        loadSound(SoundEffect.ButtonEventSound.GameLost)
        loadSound(SoundEffect.ButtonEventSound.MoveUndone)
        loadSound(SoundEffect.ButtonEventSound.CheckpointReverted)
    }

    @Subscribe
    fun on(evt: MoveRecordedEvent) {
        playOnce(SoundEffect.CellClick)
    }

    @Subscribe
    fun on(evt: CellUndoneEvent) {
        playOnce(SoundEffect.ButtonEventSound.MoveUndone)
    }

    @Subscribe
    fun on(evt: RevertedToCheckpointEvent) {
        playOnce(SoundEffect.ButtonEventSound.CheckpointReverted)
    }

    @Subscribe
    fun on(evt: BoardResetEvent) {
        playOnce(SoundEffect.ButtonEventSound.BoardReset)
    }

    @Subscribe
    fun on(evt: CheckpointSetEvent) {
        playOnce(SoundEffect.ButtonEventSound.CheckpointSet)
    }

    @Subscribe
    fun on(evt: CheckpointResetEvent) {
        playOnce(SoundEffect.ButtonEventSound.CheckpointSet)
    }

    @Subscribe
    fun on(evt: GameWonEvent) {
        playOnce(SoundEffect.ButtonEventSound.GameWon)
    }

    @Subscribe
    fun on(evt: GameLostEvent) {
        playOnce(SoundEffect.ButtonEventSound.GameLost)
    }

    @Subscribe
    fun on(evt: FreebiePlacedEvent) {
        playOnce(SoundEffect.ButtonEventSound.FreebiePlaced)
    }
}