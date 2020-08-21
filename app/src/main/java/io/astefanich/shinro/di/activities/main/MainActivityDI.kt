package io.astefanich.shinro.di.activities.main


import android.content.Context
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.astefanich.shinro.di.PerActivity
import io.astefanich.shinro.di.activities.main.fragments.GameComponent
import io.astefanich.shinro.di.activities.main.fragments.GameSummaryComponent
import io.astefanich.shinro.ui.MainActivity
import timber.log.Timber
import javax.inject.Named

@PerActivity
@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent {

    fun inject(activity: MainActivity)

    abstract fun getGameSummaryComponentBuilder(): GameSummaryComponent.Builder

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun actitivtyContext(@Named("actCtx") ctx: Context): Builder

        fun build(): MainActivityComponent
    }
}



@Module
object MainActivityModule {

    init {
        Timber.i("Main activity module init")
    }

    @PerActivity
    @Provides
    fun providesNums(): List<Int> = listOf(1,2,3)

//    @PerActivity
//    fun providesBus(): EventBus = EventBus.getDefault()

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
