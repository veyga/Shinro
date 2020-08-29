package io.astefanich.shinro

import android.app.Application
import io.astefanich.shinro.di.app.AppComponent
import io.astefanich.shinro.di.app.DaggerAppComponent
import timber.log.Timber

class ShinroApplication : Application() {

//            lateinit var appComponent: AppComponent
    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }

    //
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        appComponent.inject(this)
    }
}

