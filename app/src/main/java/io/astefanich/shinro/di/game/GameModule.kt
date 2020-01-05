package io.astefanich.shinro.di.game

import dagger.Binds
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.di.GameFragmentScope

@Module
class GameModule(private val boardId: Int) {

    @GameFragmentScope
    @Provides
    fun providesBoardId(): Int = boardId
}
//@Module
//interface GameModule {
//
//    @Binds
//    fun boardId(boardId: Int): Int
//}
