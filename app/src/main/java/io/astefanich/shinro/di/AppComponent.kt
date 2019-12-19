package io.astefanich.shinro.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.astefanich.shinro.ShinroApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ActivityInjectorsModule::class,
        AppModule::class
    ]
)
interface AppComponent : AndroidInjector<ShinroApplication> {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }

}

