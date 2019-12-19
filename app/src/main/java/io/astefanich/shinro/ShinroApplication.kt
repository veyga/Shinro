package io.astefanich.shinro

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.astefanich.shinro.di.DaggerShinroAppComponent
import timber.log.Timber

class ShinroApplication : DaggerApplication() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<ShinroApplication> {
        return DaggerShinroAppComponent.builder().build()
    }

}

