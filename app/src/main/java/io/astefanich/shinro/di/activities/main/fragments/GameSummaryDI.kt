package io.astefanich.shinro.di.activities.main.fragments


import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.*
import dagger.multibindings.IntoMap
import io.astefanich.shinro.common.GameSummary
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.di.ViewModelKey
import io.astefanich.shinro.ui.GameSummaryFragment
import io.astefanich.shinro.util.sound.AbstractSoundPlayer
import io.astefanich.shinro.util.sound.SoundEffect
import io.astefanich.shinro.util.sound.SoundPlayer
import io.astefanich.shinro.viewmodels.GameSummaryViewModel
import javax.inject.Named

@PerFragment
@Subcomponent(
    modules = [GameSummaryModule::class, GameSummaryViewModelModule::class]
)
interface GameSummaryComponent {

    fun inject(frag: GameSummaryFragment)

    @Subcomponent.Builder
    interface Builder {

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

    @PerFragment
    @Provides
    @Named("gameSummarySoundPlayer")
    fun providesGameSoundPlayer(
        prefs: SharedPreferences,
        @Named("actCtx") ctx: Context
    ): SoundPlayer =
        object : AbstractSoundPlayer(ctx, prefs, 1) {
            init {
                loadSound(SoundEffect.ButtonEventSound.ScoreClick)
            }
        }

    @Provides
    fun providesDifficultiesReprs(): Array<String> = arrayOf("EASY", "MEDIUM", "HARD")

}
