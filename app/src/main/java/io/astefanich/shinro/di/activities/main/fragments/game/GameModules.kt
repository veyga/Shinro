package io.astefanich.shinro.di.activities.main.fragments.game

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.astefanich.shinro.common.TimeSeconds
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.di.ViewModelKey
import io.astefanich.shinro.util.ShinroTimer
import io.astefanich.shinro.viewmodels.GameViewModel
import javax.inject.Named

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
    fun providesToaster(@Named("appCtx") ctx: Context): (String) -> Unit =
        { msg -> Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show() }

    @PerFragment
    @Provides
    @Named("gameTimer")
    fun providesGameTimer(): ShinroTimer = ShinroTimer(TimeSeconds.ONE)

//    @PerFragment
//    @Provides
//    @Named
//    fun providesUITimerPeriod(prefs: SharedPreferences): TimeSeconds {
//
//    }

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
}

