package io.astefanich.shinro.di.activities.main.fragments


import androidx.lifecycle.ViewModel
import dagger.*
import dagger.multibindings.IntoMap
import io.astefanich.shinro.common.Difficulty
import io.astefanich.shinro.common.GameSummary
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.di.ViewModelKey
import io.astefanich.shinro.ui.GameSummaryFragment
import io.astefanich.shinro.viewmodels.GameSummaryViewModel

//@PerFragment
//@Component(
//    dependencies = [AppComponent::class],
//    modules = [GameSummaryModule::class, GameSummaryViewModelModule::class])
//interface GameSummaryComponent : AndroidInjector<GameSummaryFragment> {
@PerFragment
@Subcomponent(
    modules = [GameSummaryModule::class, GameSummaryViewModelModule::class]
)
interface GameSummaryComponent {

    fun inject(frag: GameSummaryFragment)

    @Subcomponent.Builder
    interface Builder {

//        @BindsInstance
//        fun difficulty(diff: Difficulty): Builder
//
//        @BindsInstance
//        fun win(win: Boolean): Builder
//
//        @BindsInstance
//        fun time(time: Long): Builder

        @BindsInstance
        fun gameSummary(summary: GameSummary): Builder

        fun build(): GameSummaryComponent
    }
}

@Module
abstract class GameSummaryViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameSummaryViewModel::class)
    abstract fun bindGameSummaryViewModel(gameViewModel: GameSummaryViewModel): ViewModel
}


@Module
class GameSummaryModule {

    //provide way to keep track of user's total score
    @Provides
    fun providesDifficultiesReprs(): Array<String> = arrayOf("EASY", "MEDIUM", "HARD")

    @Provides
    fun providesChangeDifficultyPrompt(): String = "Change Difficulty:"
}
