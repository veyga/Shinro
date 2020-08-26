package io.astefanich.shinro.di.activities.main


import android.content.Context
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.games.AchievementsClient
import com.google.android.gms.games.Games
import com.google.android.gms.games.LeaderboardsClient
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.astefanich.shinro.di.PerActivity
import io.astefanich.shinro.di.activities.main.fragments.GameComponent
import io.astefanich.shinro.di.activities.main.fragments.GameSummaryComponent
import io.astefanich.shinro.di.activities.main.fragments.StatisticsComponent
import io.astefanich.shinro.ui.MainActivity
import io.astefanich.shinro.ui.TitleFragment
import timber.log.Timber
import javax.inject.Named

@PerActivity
@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent {

    fun inject(activity: MainActivity)

    fun inject(titleFragment: TitleFragment)

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

    @Provides
    fun providesGoogleSignInOption(): GoogleSignInOptions =
        GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN

    @Provides
    fun providesLastSignedInAccount(@Named("actCtx") ctx: Context): GoogleSignInAccount? =
        GoogleSignIn.getLastSignedInAccount(ctx)

    @Provides
    fun providesGoogleSignInClient(
        @Named("actCtx") ctx: Context,
        options: GoogleSignInOptions
    ): GoogleSignInClient =
        GoogleSignIn.getClient(ctx, options)

    @Provides
    fun providesLeaderboardsClient(
        @Named("actCtx") ctx: Context,
        googleSignInAccount: GoogleSignInAccount?
    ): Option<LeaderboardsClient> {
        return if (googleSignInAccount == null) {
            Timber.i("leader: last signed in account is null")
            None
        } else {
            Timber.i("leader: last signed in account is NOT null")
            Some(Games.getLeaderboardsClient(ctx, googleSignInAccount))
        }
    }

    @Provides
    fun providesAchievementsClient(
        @Named("actCtx") ctx: Context,
        googleSignInAccount: GoogleSignInAccount?
    ): Option<AchievementsClient> {
        return if (googleSignInAccount == null) {
            Timber.i("achieve: last signed in account is null")
            None
        } else {
            Timber.i("achieve: last signed in account is NOT null")
            Some(Games.getAchievementsClient(ctx, googleSignInAccount))
        }
    }
}



