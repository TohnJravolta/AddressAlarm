package org.addressalarm.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.addressalarm.R

class SettingsFragment : PreferenceFragmentCompat() {

    private val gigApps = setOf(
        "com.ubercab.driver",
        "com.lyft.android.driver",
        "com.doordash.driverapp",
        "com.instacart.shopper"
    )

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        setupAppSelectionPreference()
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference is SearchableMultiSelectListPreference) {
            val dialogFragment = SearchableMultiSelectListPreferenceDialogFragmentCompat.newInstance(preference.key, gigApps)
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(parentFragmentManager, null)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    private fun setupAppSelectionPreference() {
        findPreference<SearchableMultiSelectListPreference>("selected_apps")?.apply {
            val pm = requireActivity().packageManager
            // Enumerate all launcher activities (user-facing apps). Package visibility
            // is granted via <queries> in AndroidManifest.xml.
            val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
            val resolveInfos = pm.queryIntentActivities(launcherIntent, 0)

            // Some packages may expose multiple launcher activities. Deduplicate by package.
            val apps = resolveInfos
                .mapNotNull { it.activityInfo?.applicationInfo }
                .distinctBy { it.packageName }
                .sortedBy { it.loadLabel(pm).toString().lowercase() }
            
            val entries = apps.map { app ->
                val appName = app.loadLabel(pm).toString()
                if (gigApps.contains(app.packageName)) {
                    "$appName (Gig App)"
                } else {
                    appName
                }
            }.toTypedArray()
            
            val entryValues = apps.map { it.packageName }.toTypedArray()
            
            this.entries = entries
            this.entryValues = entryValues
        }
    }
}
