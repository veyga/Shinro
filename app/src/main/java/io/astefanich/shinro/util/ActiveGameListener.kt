package io.astefanich.shinro.util

import android.content.SharedPreferences
import io.astefanich.shinro.common.GameLoadedEvent
import io.astefanich.shinro.common.GameTornDownEvent
import io.astefanich.shinro.di.PerActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@PerActivity
class ActiveGameListener
@Inject
constructor(
    val prefs: SharedPreferences,
    val bus: EventBus,
) {

    init {
        bus.register(this)
    }

    private val KEY = "has_active_game"

    val hasActiveGame = { prefs.getBoolean(KEY, false) }

    @Subscribe
    fun handle(gameLoadedEvent: GameLoadedEvent) {
        prefs.edit().putBoolean(KEY, true).apply()
    }

    @Subscribe
    fun handle(gameTornDownEvent: GameTornDownEvent) {
        prefs.edit().putBoolean(KEY, false).apply()
    }

}