package io.astefanich.shinro.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import io.astefanich.shinro.ShinroApplication

@AppScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityInjectorsModule::class,
        AppModule::class,
        ViewModelModule::class,
        InstructionsModule::class
    ]
)
interface AppComponent : AndroidInjector<ShinroApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}

