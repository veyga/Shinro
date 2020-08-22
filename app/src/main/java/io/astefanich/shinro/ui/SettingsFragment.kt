package io.astefanich.shinro.ui

import android.os.Bundle
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import io.astefanich.shinro.R
import io.astefanich.shinro.common.PlayRequest

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(
                SettingsFragmentDirections.actionSettingsToGame(PlayRequest.Resume)
            )
        }
    }
}