package io.astefanich.shinro.di.activities.main.fragments

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.astefanich.shinro.di.PerFragment
import io.astefanich.shinro.ui.TitleFragment
import javax.inject.Named

@PerFragment
@Subcomponent(modules = [TitleModule::class])
interface TitleComponent {

    fun inject(fragment: TitleFragment)

}

@Module
class TitleModule {

    @Provides
    fun providesGoogleSignInOption(): GoogleSignInOptions =
        GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN

    @Provides
    fun providesGoogleSignInClient(
        @Named("actCtx") ctx: Context,
        options: GoogleSignInOptions
    ): GoogleSignInClient =
        GoogleSignIn.getClient(ctx, options)
}