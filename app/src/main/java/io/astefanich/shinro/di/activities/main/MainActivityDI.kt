package io.astefanich.shinro.di.activities.main


import android.content.Context
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.games.AchievementsClient
import com.google.android.gms.games.Games
import com.google.android.gms.games.GamesClient
import com.google.android.gms.games.LeaderboardsClient
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.astefanich.shinro.di.PerActivity
import io.astefanich.shinro.di.activities.main.fragments.GameComponent
import io.astefanich.shinro.di.activities.main.fragments.GameSummaryComponent
import io.astefanich.shinro.di.activities.main.fragments.StatisticsComponent
import io.astefanich.shinro.di.activities.main.fragments.TitleComponent
import io.astefanich.shinro.ui.MainActivity
import timber.log.Timber
import javax.inject.Named

@PerActivity
@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent {

    fun inject(activity: MainActivity)

    fun getGameSummaryComponentBuilder(): GameSummaryComponent.Builder

    fun getGameComponent(): GameComponent

    fun getStatisticsComponent(): StatisticsComponent

    fun getTitleComponent(): TitleComponent

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun activityContext(@Named("actCtx") ctx: Context): Builder

        fun build(): MainActivityComponent
    }
}


//@Module(subcomponents = [TitleComponent::class])
@Module
object MainActivityModule {


    @Provides
    fun providesLastSignedInAccount(@Named("actCtx") ctx: Context): GoogleSignInAccount? =
        GoogleSignIn.getLastSignedInAccount(ctx)

    @Provides
    fun providesGamesClient(
        @Named("actCtx") ctx: Context,
        googleSignInAccount: GoogleSignInAccount?
    ): Option<GamesClient> {
        return if (googleSignInAccount == null)
            None
        else
            Some(Games.getGamesClient(ctx, googleSignInAccount))

    }


    @Provides
    fun providesLeaderboardsClient(
        @Named("actCtx") ctx: Context,
        googleSignInAccount: GoogleSignInAccount?
    ): Option<LeaderboardsClient> {

        return if (googleSignInAccount == null) {
            Timber.i("last sign in account == null")
            None
        } else {
            Timber.i("last sign in account not null")
            Some(Games.getLeaderboardsClient(ctx, googleSignInAccount))
        }
    }

    @Provides
    fun providesAchievementsClient(
        @Named("actCtx") ctx: Context,
        googleSignInAccount: GoogleSignInAccount?
    ): Option<AchievementsClient> {
        return if (googleSignInAccount == null) {
            Timber.i("last sign in account == null")
            None
        } else {
            Timber.i("last sign in account not null")
            Some(Games.getAchievementsClient(ctx, googleSignInAccount))
        }
    }
}



