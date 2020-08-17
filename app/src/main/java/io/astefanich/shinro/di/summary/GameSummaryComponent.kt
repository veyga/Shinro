package io.astefanich.shinro.di.summary

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import io.astefanich.shinro.domain.GameSummary
import io.astefanich.shinro.ui.GameSummaryFragment

//@PerFragment
//@Component(modules = [TipsModule::class])
@Component(
    modules = [GameSummaryModule::class, GameSummaryViewModelModule::class])
interface GameSummaryComponent : AndroidInjector<GameSummaryFragment> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun gameSummary(summary: GameSummary): Builder

        fun build(): GameSummaryComponent
    }
}
