package io.astefanich.shinro.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.astefanich.shinro.ui.MainActivity
import io.astefanich.shinro.ui.game.GameFragment

@Module
interface ShinroAppModule {

    @ContributesAndroidInjector
    fun contributeMainActivityInjector(): MainActivity

    @ContributesAndroidInjector
    fun contributeGameFragmentInjector(): GameFragment
//    fun gameFragment(): GameFragment
}