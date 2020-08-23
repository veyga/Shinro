package io.astefanich.shinro.util.sound

import io.astefanich.shinro.R

sealed class SoundEffect(val raw: Int) {
    object CellClick : SoundEffect(R.raw.click)
    sealed class ButtonEventSound(raw: Int) : SoundEffect(raw) {
        object CheckpointSet : ButtonEventSound(R.raw.checkpoint_set)
        object FreebiePlaced : ButtonEventSound(R.raw.freebie)
        object GameWon : ButtonEventSound(R.raw.game_won)
        object BoardReset : ButtonEventSound(R.raw.reset)
        object GameLost : ButtonEventSound(R.raw.surrender)
        object MoveUndone : ButtonEventSound(R.raw.undo)
        object CheckpointReverted : ButtonEventSound(R.raw.undo_checkpoint)
    }
}
