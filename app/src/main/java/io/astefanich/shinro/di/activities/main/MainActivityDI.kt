package io.astefanich.shinro.di.activities.main


import android.content.Context
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import io.astefanich.shinro.di.PerActivity
import io.astefanich.shinro.di.activities.main.fragments.GameComponent
import io.astefanich.shinro.di.activities.main.fragments.GameSummaryComponent
import io.astefanich.shinro.di.activities.main.fragments.StatisticsComponent
import io.astefanich.shinro.ui.MainActivity
import javax.inject.Named

@PerActivity
@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent {

    fun inject(activity: MainActivity)

    fun getGameSummaryComponentBuilder(): GameSummaryComponent.Builder

    fun getGameComponent(): GameComponent

    fun getStatisticsComponent(): StatisticsComponent

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun activityContext(@Named("actCtx") ctx: Context): Builder

        fun build(): MainActivityComponent
    }
}


@Module
object MainActivityModule {


}
