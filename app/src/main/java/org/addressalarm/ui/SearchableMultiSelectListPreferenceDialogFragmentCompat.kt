package org.addressalarm.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.preference.MultiSelectListPreference
import androidx.preference.PreferenceDialogFragmentCompat
import org.addressalarm.R

class SearchableMultiSelectListPreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat() {

    private lateinit var entries: Array<CharSequence>
    private lateinit var entryValues: Array<CharSequence>
    private val selectedValues = HashSet<String>()
    private lateinit var adapter: ArrayAdapter<CharSequence>
    private lateinit var valueByLabel: Map<String, String>

    companion object {
        fun newInstance(key: String, gigApps: Set<String>): SearchableMultiSelectListPreferenceDialogFragmentCompat {
            val fragment = SearchableMultiSelectListPreferenceDialogFragmentCompat()
            val args = Bundle(2)
            args.putString(ARG_KEY, key)
            args.putStringArrayList("gigApps", ArrayList(gigApps))
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var gigApps: Set<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preference = preference as MultiSelectListPreference
        entries = preference.entries
        entryValues = preference.entryValues
        selectedValues.addAll(preference.values)
        gigApps = arguments?.getStringArrayList("gigApps")?.toSet() ?: emptySet()
        
        // Build the mapping from label to value
        valueByLabel = entries.indices.associate { i ->
            entries[i].toString() to entryValues[i].toString()
        }
    }

    override fun onCreateDialogView(context: Context): View {
        val view = View.inflate(context, R.layout.searchable_multi_select_list_preference_dialog, null)
        val searchView = view.findViewById<SearchView>(R.id.search_view)
        val listView = view.findViewById<ListView>(android.R.id.list)

        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_multiple_choice, entries.toList())
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        fun syncCheckedState() {
            for (i in 0 until adapter.count) {
                val label = adapter.getItem(i)?.toString() ?: continue
                val value = valueByLabel[label] ?: continue
                listView.setItemChecked(i, selectedValues.contains(value))
            }
        }

        // Initialize checked state before any filtering
        syncCheckedState()

        // Fixed: Ensure we get the correct item from the filtered adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // Get the label directly from the filtered adapter at the clicked position
            val label = adapter.getItem(position)?.toString()
            if (label == null) return@OnItemClickListener
            
            // Look up the corresponding value for this label
            val value = valueByLabel[label]
            if (value == null) return@OnItemClickListener

            // Update the selection based on the current checked state
            // Note: ListView automatically toggles the checked state before this listener is called
            if (listView.isItemChecked(position)) {
                selectedValues.add(value)
                if (gigApps.contains(value)) {
                    showGigAppWarning()
                }
            } else {
                selectedValues.remove(value)
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText) {
                    // After filter publishes results, realign checks to current list
                    syncCheckedState()
                }
                return true
            }
        })

        return view
    }

    private fun showGigAppWarning() {
        AlertDialog.Builder(requireContext())
            .setTitle("Gig App Warning")
            .setMessage("Monitoring gig apps can be risky. AddressAlarm provides reminders so you can make informed decisions; it never auto-declines or accepts work for you.")
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val preference = preference as MultiSelectListPreference
            if (preference.callChangeListener(selectedValues)) {
                preference.values = selectedValues
            }
        }
    }
}
