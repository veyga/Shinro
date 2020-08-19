package io.astefanich.shinro.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import io.astefanich.shinro.ShinroApplication
import io.astefanich.shinro.di.activities.ActivityInjectorsModule

@PerApplication
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ActivityInjectorsModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<ShinroApplication> {

//    fun getActivityComponentBuilder(): MainActivityComponent.Builder

//    fun getGameComponentBuilder(): GameComponent.Builder

//    fun getResultsDao(): ResultsDao

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}

