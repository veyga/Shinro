package io.astefanich.shinro.util

import android.content.SharedPreferences
import io.astefanich.shinro.common.GameLoadedEvent
import io.astefanich.shinro.common.GameTornDownEvent
import io.astefanich.shinro.di.PerActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
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
        Timber.i("got gameLoadedEvent: setting active to true")
        prefs.edit().putBoolean(KEY, true).apply()
    }

    @Subscribe
    fun handle(gameTornDownEvent: GameTornDownEvent) {
        Timber.i("got gameTornDownEvent: setting active to false")
        prefs.edit().putBoolean(KEY, false).apply()
    }

}