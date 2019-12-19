package io.astefanich.shinro

import dagger.android.DaggerApplication
import io.astefanich.shinro.di.AppComponent
import io.astefanich.shinro.di.DaggerAppComponent
import timber.log.Timber

class ShinroApplication : DaggerApplication() {

    private val appInjector = DaggerAppComponent.builder().build()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
    override fun applicationInjector(): AppComponent   = appInjector

//    override fun applicationInjector(): AndroidInjector<ShinroApplication> {
//        return DaggerAppComponent.builder().build()
//    }

}

