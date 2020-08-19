package io.astefanich.shinro.di.activities.main.fragments.summary

import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import dagger.android.AndroidInjector
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.ui.GameSummaryFragment

//@PerFragment
//@Component(
//    dependencies = [AppComponent::class],
//    modules = [GameSummaryModule::class, GameSummaryViewModelModule::class])
//interface GameSummaryComponent : AndroidInjector<GameSummaryFragment> {
@PerFragment
@Subcomponent(
    modules = [GameSummaryModule::class, GameSummaryViewModelModule::class]
)
interface GameSummaryComponent : AndroidInjector<GameSummaryFragment> {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun difficulty(diff: Difficulty): Builder

        @BindsInstance
        fun win(win: Boolean): Builder

        @BindsInstance
        fun time(time: Long): Builder

        fun build(): GameSummaryComponent
    }
}
