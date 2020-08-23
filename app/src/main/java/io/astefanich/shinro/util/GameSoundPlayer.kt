package io.astefanich.shinro.util

import android.content.Context
import android.content.SharedPreferences
import io.astefanich.shinro.R
import io.astefanich.shinro.viewmodels.*
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject
import javax.inject.Named


class GameSoundPlayer
@Inject
constructor(
    prefs: SharedPreferences,
    @Named("actCtx")ctx: Context
) : SoundPlayer(prefs, ctx, 8) {


    init {
        loadSound(SoundEffect.CellClick, R.raw.click)
        loadSound(SoundEffect.ButtonEventSound.CheckpointSet, R.raw.checkpoint_set)
        loadSound(SoundEffect.ButtonEventSound.FreebiePlaced, R.raw.freebie)
        loadSound(SoundEffect.ButtonEventSound.GameWon, R.raw.game_won)
        loadSound(SoundEffect.ButtonEventSound.BoardReset, R.raw.reset)
        loadSound(SoundEffect.ButtonEventSound.GameLost, R.raw.surrender)
        loadSound(SoundEffect.ButtonEventSound.MoveUndone, R.raw.undo)
        loadSound(SoundEffect.ButtonEventSound.CheckpointReverted, R.raw.undo_checkpoint)
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