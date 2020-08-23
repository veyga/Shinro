package io.astefanich.shinro.di.activities.main.fragments


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Vibrator
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import io.astefanich.shinro.common.Cell
import io.astefanich.shinro.common.Grid
import io.astefanich.shinro.common.TimeSeconds
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.di.ViewModelKey
import io.astefanich.shinro.ui.GameFragment
import io.astefanich.shinro.util.GameVibrator
import io.astefanich.shinro.util.ShinroTimer
import io.astefanich.shinro.util.sound.GameSoundPlayer
import io.astefanich.shinro.util.sound.SoundPlayer
import io.astefanich.shinro.viewmodels.GameViewModel
import javax.inject.Named


@PerFragment
@Subcomponent(modules = [GameModule::class, GameViewModelModule::class])
interface GameComponent {

    fun inject(frag: GameFragment)
}

//GameComponent is a sub-component, the view model needs to bound here
@Module
abstract class GameViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    abstract fun bindGameViewModel(gameViewModel: GameViewModel): ViewModel
}


@Module
object GameModule {

    @PerFragment
    @Provides
    @Named("winBuzz")
    fun providesWinBuzzPattern(): LongArray = longArrayOf(0, 500)

    @PerFragment
    @Provides
    @Named("resetBuzz")
    fun providesResetBuzzPattern(): LongArray = longArrayOf(0, 50)

    @PerFragment
    @Provides
    @JvmStatic
    fun providesToaster(@Named("actCtx") ctx: Context): (String) -> Unit =
        { msg -> Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show() }

    @PerFragment
    @Provides
    fun providesGameTimer(): ShinroTimer = ShinroTimer(TimeSeconds.ONE)

    @PerFragment
    @Provides
    fun providesStartingCheckpoint(): Grid = Array(9) { Array(9) { Cell(" ") } }


    @PerFragment
    @Provides
    fun providesUITimer(prefs: SharedPreferences): Option<ShinroTimer> {
        if (prefs.getBoolean("timer_visible", true)) {
            return when (prefs.getString("timer_increment", "")) {
                "5 seconds" -> Some(ShinroTimer(TimeSeconds.FIVE))
                "10 seconds" -> Some(ShinroTimer(TimeSeconds.TEN))
                "30 seconds" -> Some(ShinroTimer(TimeSeconds.THIRTY))
                else -> Some(ShinroTimer(TimeSeconds.ONE))
            }
        } else
            return None
    }


    @PerFragment
    @Provides
    fun providesGameDialogBuilder(@Named("actCtx") ctx: Context): (String, String, () -> Unit) -> AlertDialog.Builder {
        return { title, message, posAction ->
            AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("YES", DialogInterface.OnClickListener { dialog, id ->
                    posAction()
                })
                .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, id ->
                })
        }
    }


    @Provides
    @Named("gameSoundPlayer")
    fun providesGameSoundPlayer(
        @Named("actCtx") ctx: Context,
        prefs: SharedPreferences,
    ): Option<SoundPlayer> {
        val clicksEnabled = prefs.getBoolean("click_sound_enabled", true)
        val buttonsEventsEnabled = prefs.getBoolean("buttons_events_sound_enabled", true)
        if (!clicksEnabled && !buttonsEventsEnabled)
            return None

        return Some(GameSoundPlayer(ctx, clicksEnabled, buttonsEventsEnabled))
    }

    @PerFragment
    @Provides
    fun providesGameVibrator(
        @Named("actCtx") ctx: Context,
        prefs: SharedPreferences
    ): Option<GameVibrator> {
        val enabled = prefs.getBoolean("vibrations_enabled", false)
        if (!enabled)
            return None

        val strengthPct = prefs.getInt("vibration_strength", 50)
        var amplitude = Math.round(255 * (strengthPct / 100.0f))
        if (amplitude <= 0) {
            amplitude = 1
        }
        val sysVibrator = ctx.getSystemService<Vibrator>()
        return if (sysVibrator == null)
            None
        else Some(GameVibrator(sysVibrator!!, amplitude))
    }

}

