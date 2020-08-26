package io.astefanich.shinro.di.activities.main

import android.content.Context
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.games.Games
import com.google.android.gms.games.LeaderboardsClient
import dagger.Module
import dagger.Subcomponent
import io.astefanich.shinro.di.PerActivity
import io.astefanich.shinro.ui.MainActivity
import timber.log.Timber
import javax.inject.Named


//need to share leaderboard client between title fragment and summary component
//@PerActivity
//@Subcomponent
//interface GoogleGamesComponent {
//
//
//}
//@Module
//class GoogleGamesModule {
//
//    fun providesGoogleSignInOptions(): GoogleSignInOptions =
//        GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
//
//    fun providesGoogleSignInClient(
//        @Named("actCtx") ctx: Context,
//        googleSignInOptions: GoogleSignInOptions
//    ): GoogleSignInClient =
//        GoogleSignIn.getClient(ctx, googleSignInOptions)
//
//    fun providesGoogleSignInAccount(
//        @Named("actCtx") ctx: Context,
//        signInOptions: GoogleSignInOptions,
//        googleSignInClient: GoogleSignInClient,
//    ): GoogleSignInAccount? {
//        Timber.i("requesting google signin account")
//        var googleSignInAccount = GoogleSignIn.getLastSignedInAccount(ctx)
//        if (googleSignInAccount == null) {
//            googleSignInClient.silentSignIn()?.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    googleSignInAccount = task.result
//                } else {
//                    val intent = googleSignInClient.signInIntent
//                    var resultCode = 0
//                    ActivityCompat.startActivityForResult(
//                        ctx as MainActivity,
//                        intent,
//                        resultCode,
//                        null
//                    )
//                    Timber.i("result code is now $resultCode")
//                    if(resultCode == 0){
//                        Timber.i("you signed in yo")
//                    } else{
//
//                    }
//                    Timber.i("Silent sign in failed: ${task.exception}")
//                }
//            }
//        }
//        return googleSignInAccount
//    }
//
//    fun providesLeaderboardsClient(
//        @Named("actCtx") ctx: Context,
//        googleSignInAccount: GoogleSignInAccount?
//    ): LeaderboardsClient? {
//        Timber.i("gettings the leaderboard client")
//        return if (googleSignInAccount == null) {
//            null
//        } else {
//            Games.getLeaderboardsClient(ctx, googleSignInAccount)
//        }
//    }
//}
