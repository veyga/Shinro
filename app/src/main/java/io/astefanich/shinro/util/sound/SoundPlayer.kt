package io.astefanich.shinro.util.sound

interface SoundPlayer {
    fun loadSound(sound: SoundEffect)
    fun playOnce(sound: SoundEffect)
    fun playLoop(sound: SoundEffect, rate: Float = 2f) //loops forever
    fun pauseAll()
}
