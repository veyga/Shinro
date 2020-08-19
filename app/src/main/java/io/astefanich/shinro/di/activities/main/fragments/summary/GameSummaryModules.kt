package io.astefanich.shinro.di.activities.main.fragments.summary

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
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
class GameSummaryModule {

    //provide way to keep track of user's total score
    @Provides
    fun providesDifficultiesReprs(): Array<String> = arrayOf("EASY", "MEDIUM", "HARD")

    @Provides
    fun providesChangeDifficultyPrompt(): String = "Change Difficulty:"
}