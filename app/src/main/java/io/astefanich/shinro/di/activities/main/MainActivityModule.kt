package io.astefanich.shinro.di.activities.main

import dagger.Module
import dagger.Provides
import io.astefanich.shinro.di.PerFragment
import timber.log.Timber

@Module
object MainActivityModule {

    init {
        Timber.i("Main activity module init")
    }

    @PerFragment
    @Provides
    fun providesADouble(): List<Int> = listOf(1,2,3)
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