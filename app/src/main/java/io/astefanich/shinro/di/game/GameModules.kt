package io.astefanich.shinro.di.game

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.di.ViewModelKey
import io.astefanich.shinro.util.GameTimer
import io.astefanich.shinro.viewmodels.GameViewModel
import javax.inject.Named

//Since GameComponent is a sub-component, the view model needs to bound here
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
    fun providesTimerPeriodSeconds(): Long = 1L

    @PerFragment
    @Provides
    @JvmStatic
    fun providesGameTimer(period: Long): GameTimer = GameTimer(period)
}

