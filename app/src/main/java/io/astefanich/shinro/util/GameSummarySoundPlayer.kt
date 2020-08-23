package io.astefanich.shinro.util

import android.content.Context
import android.content.SharedPreferences
import io.astefanich.shinro.R
import javax.inject.Inject
import javax.inject.Named

class GameSummarySoundPlayer
@Inject
constructor(
    prefs: SharedPreferences,
    @Named("actCtx") ctx: Context
) : SoundPlayer(prefs, ctx, 1) {

    init {
        loadSound(SoundEffect.ButtonEventSound.GameWon, R.raw.click)
    }

    fun playWinSound() {
        super.playLoop(SoundEffect.ButtonEventSound.GameWon,  2f)
    }

    fun pauseWinSound(){
        super.pauseAll()
    }
}
