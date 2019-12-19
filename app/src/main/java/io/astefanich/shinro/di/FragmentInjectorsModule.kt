package io.astefanich.shinro.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.astefanich.shinro.ui.game.GameFragment
import io.astefanich.shinro.ui.title.TitleFragment

@Module
abstract class FragmentInjectorsModule {

    @ContributesAndroidInjector
    abstract fun providesTitleFragment(): TitleFragment

    @ContributesAndroidInjector
    abstract fun providesGameFragment(): GameFragment

}