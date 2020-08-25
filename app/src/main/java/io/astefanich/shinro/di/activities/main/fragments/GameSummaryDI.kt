package io.astefanich.shinro.di.activities.main.fragments


import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import dagger.*
import dagger.multibindings.IntoMap
import io.astefanich.shinro.common.GameSummary
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.di.app.ViewModelKey
import io.astefanich.shinro.ui.GameSummaryFragment
import io.astefanich.shinro.util.sound.*
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


    @Provides
    @Named("gameSummarySoundPlayer")
    fun providesGameSummarySoundPlayer(
        @Named("actCtx") ctx: Context,
        prefs: SharedPreferences,
    ): Option<SoundPlayer> {
        return if (!prefs.getBoolean("buttons_events_sound_enabled", true))
            None
        else {
            val player =
                object : AbstractSoundPlayer(ctx, buttonsEventsEnabled = true, numStreams = 1) {
                    init {
                        loadSound(SoundEffect.ButtonEventSound.ScoreClick)
                    }
                }
            Some(player)
        }
    }

    @Provides
    fun providesDifficultiesReprs(): Array<String> = arrayOf("EASY", "MEDIUM", "HARD")

}
