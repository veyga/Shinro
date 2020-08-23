package io.astefanich.shinro.util.sound

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool

abstract class AbstractSoundPlayer(
    val ctx: Context,
    prefs: SharedPreferences,
    numStreams: Int
) : SoundPlayer {

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

    override fun loadSound(sound: SoundEffect) {
        when (sound) {
            is SoundEffect.CellClick -> {
                if (clicksEnabled)
                    soundMap.put(sound, soundPool.load(ctx, sound.raw, 1))
            }
            else -> {
                if (buttonsEventsEnabled)
                    soundMap.put(sound, soundPool.load(ctx, sound.raw, 1))
            }
        }
    }

    override fun playOnce(sound: SoundEffect) {
        if (soundMap.containsKey(sound))
            soundPool.play(soundMap[sound]!!, .3f, .3f, 0, 0, 1f)
    }

    override fun playLoop(sound: SoundEffect, rate: Float) {
        if (soundMap.containsKey(sound))
            soundPool.play(soundMap[sound]!!, .3f, .3f, 0, -1, rate)
    }

    override fun pauseAll() {
        soundPool.autoPause()
    }

}