package io.astefanich.shinro.di.app

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import io.astefanich.shinro.ShinroApplication
import io.astefanich.shinro.di.PerApplication
import io.astefanich.shinro.di.ViewModelModule
import io.astefanich.shinro.di.activities.main.MainActivityComponent

@PerApplication
@Component(
    modules = [
        AppModule::class,
        DBModule::class,
        BoardGenModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    fun inject(app: ShinroApplication)

    fun getMainActivityComponentBuilder(): MainActivityComponent.Builder

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}

