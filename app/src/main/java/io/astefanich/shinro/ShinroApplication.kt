package io.astefanich.shinro

import dagger.android.DaggerApplication
import io.astefanich.shinro.di.AppComponent
import io.astefanich.shinro.di.DaggerAppComponent
import timber.log.Timber

class ShinroApplication : DaggerApplication() {

    val appComponent =
        DaggerAppComponent
            .builder()
            .application(this)
            .build()
//
    override fun applicationInjector(): AppComponent = appComponent

//
//    override fun applicationInjector(): AppComponent = TODO()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}

