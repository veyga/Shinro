package io.astefanich.shinro.di.game

import dagger.Module
import dagger.Provides
import io.astefanich.shinro.di.GameFragmentScope
import javax.inject.Named

@Module
class GameModule(private val boardId: Int) {

    @GameFragmentScope
    @Provides
    fun providesBoardId(): Int = boardId

    @GameFragmentScope
    @Provides
    @Named("lastVisitedFile")
    fun providesLastVisitedFileName(): String = "last_visited.txt"
}
