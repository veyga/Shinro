package io.astefanich.shinro.di.activities.main.fragments.game

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.astefanich.shinro.common.TimeSeconds
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.di.ViewModelKey
import io.astefanich.shinro.util.GameTimer
import io.astefanich.shinro.viewmodels.GameViewModel
import timber.log.Timber
import javax.inject.Named

//Since GameComponent is a sub-component, the view model needs to bound here
@Module
abstract class GameViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    abstract fun bindGameViewModel(gameViewModel: GameViewModel): ViewModel
}

//data class DialogParams(val title: String, val message: String, val posAction: () -> Unit)

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

//    @PerFragment
//    @Provides
//    fun providesTimerPeriodSeconds(): TimePeriod = TimePeriod.ONE

    @PerFragment
    @Provides
    @JvmStatic
    fun providesToaster(@Named("appCtx") ctx: Context): (String) -> Unit {
        Timber.i("game module has the context now")
        return { msg -> Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show() }
    }

    //    @Named("gameTimer")
    @PerFragment
    @Provides
    @JvmStatic
    fun providesGameTimer(): GameTimer = GameTimer(TimeSeconds.ONE)

    @Provides
    fun providesGameDialogBuilder(@Named("actCtx")ctx: Context): (String, String, () -> Unit) -> AlertDialog.Builder {
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

