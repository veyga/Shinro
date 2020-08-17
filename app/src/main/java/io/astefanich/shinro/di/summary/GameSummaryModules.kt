package io.astefanich.shinro.di.summary

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import io.astefanich.shinro.di.ViewModelKey
import io.astefanich.shinro.viewmodels.GameSummaryViewModel

@Module
abstract class GameSummaryViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameSummaryViewModel::class)
    abstract fun bindGameSummaryViewModel(gameViewModel: GameSummaryViewModel): ViewModel
}


@Module
abstract class GameSummaryModule {

    //provide way to keep track of user's total score
}