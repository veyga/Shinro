package io.astefanich.shinro.di.activities

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.astefanich.shinro.di.activities.main.fragments.MainActivityFragmentInjectorsModule
import io.astefanich.shinro.ui.MainActivity

@Module
abstract class ActivityInjectorsModule {

    @ContributesAndroidInjector(modules=[MainActivityFragmentInjectorsModule::class])
    abstract fun contributeMainActivityInjector(): MainActivity

}