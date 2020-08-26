package io.astefanich.shinro.di.activities.main


import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
import timber.log.Timber
import javax.inject.Named

@PerActivity
@Subcomponent(modules = [MainActivityModule::class])//, GoogleGamesModule::class])
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

@Module
class GoogleGamesModule {

    fun providesGoogleSignInOptions(): GoogleSignInOptions =
        GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN

    fun providesGoogleSignInClient(
        @Named("actCtx") ctx: Context,
        googleSignInOptions: GoogleSignInOptions
    ): GoogleSignInClient =
        GoogleSignIn.getClient(ctx, googleSignInOptions)

    fun providesGoogleSignInAccount(
        @Named("actCtx") ctx: Context,
        signInOptions: GoogleSignInOptions,
        googleSignInClient: GoogleSignInClient,
        ): GoogleSignInAccount? {
        Timber.i("requesting google signin account")
        var googleSignInAccount = GoogleSignIn.getLastSignedInAccount(ctx)
        if (googleSignInAccount == null) {
            googleSignInClient.silentSignIn()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    googleSignInAccount = task.result
                } else {
                    val intent = googleSignInClient.signInIntent
                    var resultCode = 0
                    startActivityForResult(ctx as MainActivity, intent, resultCode, null)
                    Timber.i("result code is now $resultCode")
                    if(resultCode == 0){
                       Timber.i("you signed in yo")
                    } else{

                    }
                    Timber.i("Silent sign in failed: ${task.exception}")
                }
            }
        }
        return googleSignInAccount
    }

    fun providesLeaderboardsClient(
        @Named("actCtx") ctx: Context,
        googleSignInAccount: GoogleSignInAccount?
    ): LeaderboardsClient? {
        Timber.i("gettings the leaderboard client")
        return if (googleSignInAccount == null) {
            null
        } else {
            Games.getLeaderboardsClient(ctx, googleSignInAccount)
        }
    }
}

