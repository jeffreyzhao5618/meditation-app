package com.cdtgrss.meditationapp.screens.timersettings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.cdtgrss.meditationapp.R

class TimerPreferencesSettingsFragment : PreferenceFragmentCompat() {

    // Inflate preferences on create
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_timer_settings, rootKey)
    }

    // Disable scrolling
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView.overScrollMode = View.OVER_SCROLL_NEVER
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        // Change timer sound preference
        if(preference?.key == "timer_sound") {
            val oldSoundUri: Uri = Uri.parse(PreferenceManager.getDefaultSharedPreferences(activity)
                .getString(resources.getString(R.string.timer_sound_key),
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()))
            startForResult.launch(oldSoundUri)
        }

        return super.onPreferenceTreeClick(preference)
    }

    // ActivityResultLauncher
    private val startForResult = registerForActivityResult(PickRingtoneContract()) { newSoundUri ->
        if (newSoundUri != null) {
            Log.i("TimerPreferences", newSoundUri)
            PreferenceManager.getDefaultSharedPreferences(activity).edit()
                .putString(resources.getString(R.string.timer_sound_key), newSoundUri).apply()
        }
    }
}

// Contract for ringtone picker
// Launch method should take in one Uri parameter of the currently set timer sound uri in
// shared preferences.
class PickRingtoneContract : ActivityResultContract<Uri, String?>() {
    override fun createIntent(context: Context, oldSoundUri: Uri) =
        Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, oldSoundUri)
            putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, oldSoundUri)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Timer Sound")
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
        }

    override fun parseResult(resultCode: Int, result: Intent?) : String? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return result?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI).toString()
    }
}