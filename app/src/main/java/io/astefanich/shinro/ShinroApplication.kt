package io.astefanich.shinro

import android.app.Application
import io.astefanich.shinro.di.AppComponent
import io.astefanich.shinro.di.DaggerAppComponent
import timber.log.Timber

class ShinroApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        appComponent.inject(this)
    }
}

