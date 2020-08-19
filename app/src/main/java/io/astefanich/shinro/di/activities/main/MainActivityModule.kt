package io.astefanich.shinro.di.activities.main

import android.content.Context
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.di.AppComponent
import io.astefanich.shinro.di.PerActivity
import io.astefanich.shinro.di.PerFragment
import timber.log.Timber
import javax.inject.Named

@Module
object MainActivityModule {

    init {
        Timber.i("Main activity module init")
    }

    @PerFragment
    @Provides
    fun providesADouble(): Double = 10.0
//    @PerActivity
//    @Provides
//    fun providesActivityComponent(
//        appComponent: AppComponent,
//        @Named("actCtx") activityContext: Context
//    ): MainActivityComponent {
//        Timber.i("Main activity module has app component and activity context")
//        return appComponent.getActivityComponentBuilder().actitivtyContext(activityContext).build()
//    }
}