package io.astefanich.shinro.di.game

import dagger.Module
import dagger.Provides
import io.astefanich.shinro.di.PerFragment
import javax.inject.Named

//@Module
//class GameModule(val boardId: Int) {
//
//    @PerFragment
//    @Provides
//    fun providesBoardId(): Int = boardId
//
//    @PerFragment
//    @Provides
//    @Named("winBuzz")
//    fun providesWinBuzzPattern(): LongArray = longArrayOf(0, 500)
//
//    @PerFragment
//    @Provides
//    @Named("resetBuzz")
//    fun providesResetBuzzPattern(): LongArray = longArrayOf(0, 50)
//
//}
@Module
object GameModule {

    @PerFragment
    @Provides
    @Named("winBuzz")
    @JvmStatic
    fun providesWinBuzzPattern(): LongArray = longArrayOf(0, 500)

    @PerFragment
    @Provides
    @Named("resetBuzz")
    @JvmStatic
    fun providesResetBuzzPattern(): LongArray = longArrayOf(0, 50)

}
