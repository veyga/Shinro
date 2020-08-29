package io.astefanich.shinro.di.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.common.NetworkTimeout
import io.astefanich.shinro.di.PerApplication
import io.astefanich.shinro.util.EventBusIndex
import org.greenrobot.eventbus.EventBus
import javax.inject.Named


@Module
class AppModule {

    @PerApplication
    @Provides
    @Named("appCtx")
    internal fun providesAppContext(application: Application): Context =
        application.applicationContext

    @PerApplication
    @Provides
    fun providesSharesPreferences(@Named("appCtx") ctx: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(ctx)

    @PerApplication
    @Provides
    fun providesEventBus(): EventBus {
        EventBus.builder().addIndex(EventBusIndex()).installDefaultEventBus()
        return EventBus.getDefault()
    }

    @Provides
    fun providesNetworkTimeoutPolicy(): NetworkTimeout = NetworkTimeout(3000L)

}


