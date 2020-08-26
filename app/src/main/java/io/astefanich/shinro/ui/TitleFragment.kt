package io.astefanich.shinro.ui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import arrow.core.Option
import arrow.core.Some
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.games.Games
import com.google.android.gms.games.LeaderboardsClient
import io.astefanich.shinro.R
import io.astefanich.shinro.common.PlayRequest
import io.astefanich.shinro.databinding.FragmentTitleBinding
import timber.log.Timber
import javax.inject.Inject

class TitleFragment : Fragment() {

//    @Inject
//    lateinit var leaderboardsClient: LeaderboardsClient


    companion object {
        const val SIGN_IN_REQUEST_CODE = 1001
        const val SHOW_LEADERBOARDS_REQUEST_CODE = 1002
        const val SHOW_ACHIEVEMENTS_REQUEST_CODE = 1003
    }

    @Inject
    @JvmSuppressWildcards
    lateinit var leaderboardsClient: Option<LeaderboardsClient>

    @Inject
    @JvmSuppressWildcards
    lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var binding: FragmentTitleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)
        (activity as MainActivity)
            .mainActivityComponent
            .inject(this)

//        initGoogleClientSignIn()
//        leaderboardsClient?.submitScore(resources.getString(R.string.leaderboard_total_points_id), 3000)
//        leaderboardsClient?.submitScore("id", 3000)

        binding.playResumeChip.setOnClickListener {
            findNavController().navigate(
                TitleFragmentDirections.actionTitleToGame(PlayRequest.Resume)
            )
        }

        binding.howToPlayTipsChip.setOnClickListener {
            findNavController().navigate(
                TitleFragmentDirections.actionTitleToTipsChoice()
            )
        }

        binding.leaderboardChip.setOnClickListener {
            when (leaderboardsClient) {
                is Some -> {
                    showLeaderboard((leaderboardsClient as Some<LeaderboardsClient>).t)
                }
                else -> {
                    startActivityForResult(
                        googleSignInClient.signInIntent,
                        TitleFragment.SIGN_IN_REQUEST_CODE
                    )
                }
            }
        }

        binding.statisticsChip.setOnClickListener {
            findNavController().navigate(
                TitleFragmentDirections.actionTitleToStatistics()
            )
        }

        binding.aboutChip.setOnClickListener {
            findNavController().navigate(TitleFragmentDirections.actionTitleToAbout())
        }

        binding.lifecycleOwner = this
        return binding.root
    }

    private fun showLeaderboard(client: LeaderboardsClient) {
        Timber.i("showing leaderboard..")
        val leaderboardIntent = client.allLeaderboardsIntent
        leaderboardIntent.addOnSuccessListener { intent ->
            startActivityForResult(intent, TitleFragment.SHOW_LEADERBOARDS_REQUEST_CODE)
        }
        leaderboardIntent.addOnFailureListener { intent ->
            AlertDialog.Builder(requireActivity())
                .setMessage(R.string.signed_out)
                .setNeutralButton(android.R.string.ok, null).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.i("request code from result is $requestCode")
        if (requestCode == TitleFragment.SIGN_IN_REQUEST_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result == null) {
                Timber.i("the result is null yo")
            }
            result?.apply {
                if (isSuccess) {
                    Timber.i("sign in success . acct is $signInAccount")
                    showLeaderboard(Games.getLeaderboardsClient(requireActivity(), signInAccount!!))
                } else {
                    AlertDialog.Builder(requireActivity())
                        .setMessage(R.string.games_connection_failed)
                        .setNeutralButton(android.R.string.ok, null).show()
                }
            }
        }
    }

//    private fun showLeaderboard(signedInAccount: GoogleSignInAccount) {
////        (activity as MainActivity)
////            .getL
//        Timber.i("trying to show leaderboard client. account = $signedInAccount")
//        val client = Games.getLeaderboardsClient(requireActivity(), signedInAccount)
//        val leaderboardIntent = client.allLeaderboardsIntent
//        leaderboardIntent.addOnSuccessListener { intent ->
//            startActivityForResult(intent, TitleFragment.SHOW_LEADERBOARDS_REQUEST_CODE)
//        }
//        leaderboardIntent.addOnFailureListener { intent ->
//            Timber.i("failed to show leaderboard")
//        }
////        client.allLeaderboardsIntent.addOnSuccessListener { intent ->
////            startActivityForResult(intent, TitleFragment.SHOW_LEADERBOARDS_REQUEST_CODE)
////            TODO inject the leaderboards client
////        }
//    }


    //text reverts from bold to normal when popping/exiting from fragment
    override fun onStart() {
        super.onStart()
        if (this::binding.isInitialized) {
            Timber.i("title frag onStart")
            binding.playResumeChip.typeface = Typeface.DEFAULT_BOLD
            binding.howToPlayTipsChip.typeface = Typeface.DEFAULT_BOLD
            binding.statisticsChip.typeface = Typeface.DEFAULT_BOLD
            binding.leaderboardChip.typeface = Typeface.DEFAULT_BOLD
            binding.aboutChip.typeface = Typeface.DEFAULT_BOLD
        }
    }

}