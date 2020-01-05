package io.astefanich.shinro.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import io.astefanich.shinro.ShinroApplication
import io.astefanich.shinro.di.game.GameComponent
import io.astefanich.shinro.di.game.GameModule

@AppScope
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityInjectorsModule::class,
        AppModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<ShinroApplication> {

    fun getGameComponent(gameModule: GameModule): GameComponent

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}

