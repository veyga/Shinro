package io.astefanich.shinro.di.game

import dagger.Module
import dagger.Provides
import io.astefanich.shinro.di.PerFragment

@Module
class GameModule(private val boardId: Int) {

    @PerFragment
    @Provides
    fun providesBoardId(): Int = boardId

    @PerFragment
    @Provides
    fun providesWinBuzzPattern(): LongArray = longArrayOf(0, 500)
//    @Named("winBuzz")

//    @GameFragmentScope
//    @Provides
//    @Named("resetBuzz")
//    fun providesResetBuzzPattern(): LongArray = longArrayOf(0, 50)
}
