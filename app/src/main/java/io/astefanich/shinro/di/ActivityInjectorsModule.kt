package io.astefanich.shinro.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.astefanich.shinro.ui.MainActivity
import io.astefanich.shinro.ui.title.TitleFragment

@Module
abstract class ActivityInjectorsModule {


    @ContributesAndroidInjector
    internal abstract fun contributeMainActivityInjector(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun contributeTitleFragmentInjector(): TitleFragment
}