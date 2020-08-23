package io.astefanich.shinro.util

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool

sealed class SoundEffect {
    object CellClick : SoundEffect()
    sealed class ButtonEventSound : SoundEffect() {
        object CheckpointSet : ButtonEventSound()
        object FreebiePlaced : ButtonEventSound()
        object GameWon : ButtonEventSound()
        object BoardReset : ButtonEventSound()
        object GameLost : ButtonEventSound()
        object MoveUndone : ButtonEventSound()
        object CheckpointReverted : ButtonEventSound()
    }
}

open class SoundPlayer(
    val prefs: SharedPreferences,
    val ctx: Context,
    numStreams: Int
) {

    private lateinit var soundPool: SoundPool

    private var clicksEnabled = false

    private var buttonsEventsEnabled = false

    private val soundMap: MutableMap<SoundEffect, Int> = mutableMapOf()

    init {
        clicksEnabled = prefs.getBoolean("click_sound_enabled", false)
        buttonsEventsEnabled = prefs.getBoolean("buttons_events_sound_enabled", false)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(numStreams)
            .build()
    }

    fun loadSound(key: SoundEffect, raw: Int) {
        when (key) {
            is SoundEffect.CellClick -> {
                if (clicksEnabled)
                    soundMap.put(key, soundPool.load(ctx, raw, 1))
            }
            else -> {
                if (buttonsEventsEnabled)
                    soundMap.put(key, soundPool.load(ctx, raw, 1))
            }
        }
    }

    fun playOnce(key: SoundEffect) {
        if (soundMap.containsKey(key))
            soundPool.play(soundMap[key]!!, 1f, 1f, 0, 0, 1f)
    }

    fun playLoop(key: SoundEffect, rate: Float = 1f){
        if (soundMap.containsKey(key))
            soundPool.play(soundMap[key]!!, 1f, 1f, 0, -1, rate)
    }

    fun pauseAll(){
        soundPool.autoPause()
    }

}