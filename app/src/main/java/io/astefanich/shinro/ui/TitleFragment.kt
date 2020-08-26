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
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.games.Games
import io.astefanich.shinro.R
import io.astefanich.shinro.common.PlayRequest
import io.astefanich.shinro.databinding.FragmentTitleBinding
import timber.log.Timber

class TitleFragment : Fragment() {

    companion object {
        const val SIGN_IN_REQUEST_CODE = 1001
        const val SHOW_LEADERBOARDS_REQUEST_CODE = 1002
        const val SHOW_ACHIEVEMENTS_REQUEST_CODE = 1003
    }

    private lateinit var binding: FragmentTitleBinding
    private var googleSignInClient: GoogleSignInClient? = null
    private var googleSignInAccount: GoogleSignInAccount? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)

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
            attemptSignInAndShow()
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


    private fun showLeaderboard(signedInAccount: GoogleSignInAccount) {
//        (activity as MainActivity)
//            .getL
        Timber.i("trying to show leaderboard client. account = $signedInAccount")
        val client = Games.getLeaderboardsClient(requireActivity(), signedInAccount)
        val leaderboardIntent = client.allLeaderboardsIntent
        leaderboardIntent.addOnSuccessListener { intent ->
            startActivityForResult(intent, TitleFragment.SHOW_LEADERBOARDS_REQUEST_CODE)
        }
        leaderboardIntent.addOnFailureListener { intent ->
            Timber.i("failed to show leaderboard")
        }
//        client.allLeaderboardsIntent.addOnSuccessListener { intent ->
//            startActivityForResult(intent, TitleFragment.SHOW_LEADERBOARDS_REQUEST_CODE)
//            TODO inject the leaderboards client
//        }
    }

    private fun attemptSignInAndShow() {
        val googleSignInClient =
            GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)

        var isSuccess = false
        fun attemptSignInSilently() {
            googleSignInAccount = GoogleSignIn.getLastSignedInAccount(requireActivity())
            Timber.i("signing in silently. last known account: $googleSignInAccount")
            googleSignInClient?.silentSignIn()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.i("silent sign in succeded")
                    isSuccess = true
                    googleSignInAccount = task.result
                    showLeaderboard(googleSignInAccount!!)
                }
            }
        }

        attemptSignInSilently()
        if (!isSuccess) {
            Timber.i("signing in explicitly")
            val intent = googleSignInClient?.signInIntent
            startActivityForResult(intent, SIGN_IN_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.i("request code from result is $requestCode")
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result == null) {
                Timber.i("the result is null yo")
            }
            result?.apply {
                if (isSuccess) {
                    googleSignInAccount = signInAccount
                    showLeaderboard(googleSignInAccount!!)
                } else {
                    val message = result.status.statusMessage
                    AlertDialog.Builder(requireActivity())
                        .setMessage(R.string.games_connection_failed)
                        .setNeutralButton(android.R.string.ok, null).show()
                }
            }
        }
    }

    //text reverts from bold to normal when popping/exiting from fragment
    override fun onStart() {
        super.onStart()
        if (this::binding.isInitialized) {
            binding.playResumeChip.typeface = Typeface.DEFAULT_BOLD
            binding.howToPlayTipsChip.typeface = Typeface.DEFAULT_BOLD
            binding.statisticsChip.typeface = Typeface.DEFAULT_BOLD
            binding.leaderboardChip.typeface = Typeface.DEFAULT_BOLD
            binding.aboutChip.typeface = Typeface.DEFAULT_BOLD
        }
    }

}