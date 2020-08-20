package io.astefanich.shinro.di.activities.main

import dagger.Module
import dagger.Provides
import io.astefanich.shinro.di.PerActivity
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

@Module
object MainActivityModule {

    init {
        Timber.i("Main activity module init")
    }

    @PerActivity
    @Provides
    fun providesADouble(): List<Int> = listOf(1,2,3)

    @PerActivity
    fun providesBus(): EventBus = EventBus.getDefault()

//    @PerActivity
//    @Provides
//    fun providesSharesPreferences(@Named("actContext") ctx: Context): SharedPreferences {
////        val pref = ctx.getSharedPreferences(Context.MODE_PRIVATE)
////        return pref
////        return (ctx as MainActivity).ge
//        val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)
//        Timber.i("providing shared prefs: $prefs")
//        return prefs
////        return  ctx.getSharedPreferences(Context.MODE_PRIVATE)
//
//    }

}