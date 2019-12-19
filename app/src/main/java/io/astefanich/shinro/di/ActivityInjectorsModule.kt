package io.astefanich.shinro.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.astefanich.shinro.ui.MainActivity

@Module
abstract class ActivityInjectorsModule {

    @ContributesAndroidInjector(modules=[FragmentInjectorsModule::class])
    abstract fun contributeMainActivityInjector(): MainActivity

}