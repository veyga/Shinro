package io.astefanich.shinro.di.summary

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.di.app.AppComponent
import io.astefanich.shinro.domain.Difficulty
import io.astefanich.shinro.ui.GameSummaryFragment

@PerFragment
@Component(
    dependencies = [AppComponent::class],
    modules = [GameSummaryModule::class, GameSummaryViewModelModule::class])
interface GameSummaryComponent : AndroidInjector<GameSummaryFragment> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun difficulty(diff: Difficulty): Builder

        @BindsInstance
        fun win(win: Boolean): Builder

        @BindsInstance
        fun time(time: Long): Builder

        fun appComponent(component: AppComponent): Builder

        fun build(): GameSummaryComponent
    }
}
