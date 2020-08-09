package io.astefanich.shinro.di.game

import dagger.Module
import dagger.Provides
import io.astefanich.shinro.di.PerFragment
import javax.inject.Named

//@module
//class gamemodule(val boardid: int) {
//
//    @perfragment
//    @provides
//    fun providesboardid(): int = boardid
//
//    @perfragment
//    @provides
//    @named("winbuzz")
//    fun provideswinbuzzpattern(): longarray = longarrayof(0, 500)
//
//    @perfragment
//    @provides
//    @named("resetbuzz")
//    fun providesresetbuzzpattern(): longarray = longarrayof(0, 50)
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
