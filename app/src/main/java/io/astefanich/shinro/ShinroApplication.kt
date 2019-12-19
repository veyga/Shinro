package io.astefanich.shinro

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.astefanich.shinro.di.DaggerShinroAppComponent

class ShinroApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<ShinroApplication> {
        return DaggerShinroAppComponent.builder().build()
    }

}

