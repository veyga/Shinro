package io.astefanich.shinro.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.astefanich.shinro.R

class SettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}