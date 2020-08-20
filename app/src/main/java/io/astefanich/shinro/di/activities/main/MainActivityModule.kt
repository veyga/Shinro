package io.astefanich.shinro.di.activities.main

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import io.astefanich.shinro.di.PerActivity
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.ui.MainActivity
import timber.log.Timber
import javax.inject.Named

@Module
object MainActivityModule {

    init {
        Timber.i("Main activity module init")
    }

    @PerActivity
    @Provides
    fun providesADouble(): List<Int> = listOf(1,2,3)

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