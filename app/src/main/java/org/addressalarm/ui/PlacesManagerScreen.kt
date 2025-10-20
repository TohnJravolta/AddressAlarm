package org.addressalarm.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.ExperimentalMaterial3Api // Added for OptIn
import androidx.compose.ui.Modifier
import org.addressalarm.data.FlaggedPlace // Ensure this is imported

@OptIn(ExperimentalMaterial3Api::class) // Added OptIn as global compiler arg was removed
@Composable
fun PlacesManagerScreen(
    vm: PlacesViewModel
) {
    // Updated to use 'by' delegate and explicit type
    val places: List<FlaggedPlace> by vm.places.collectAsState()

    var pendingDelete by remember { mutableStateOf<FlaggedPlace?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AddressAlarm places") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.addSample() }) { // addSample call is fine
                Text("+")
            }
        }
    ) { inner ->
        PlacesList(
            places = places,
            onDelete = { place -> pendingDelete = place }, // ask for confirmation first
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        )
    }

    // Confirm deletion dialog
    val toDelete = pendingDelete
    if (toDelete != null) {
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("Delete entry?") },
            text = { Text("Are you sure you want to delete \"${toDelete.name ?: toDelete.rawAddress}\"? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.remove(toDelete.id)
                    pendingDelete = null
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) { Text("Cancel") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class) // ListItem is M3
@Composable
fun PlacesList(
    places: List<org.addressalarm.data.FlaggedPlace>,
    onDelete: (org.addressalarm.data.FlaggedPlace) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.lazy.LazyColumn(modifier) {
        items(places.size) { idx ->
            val p = places[idx]
            ListItem(
                headlineContent = { Text(p.name ?: p.rawAddress) },
                supportingContent = { Text(p.tags.joinToString(", ")) },
                trailingContent = {
                    TextButton(onClick = { onDelete(p) }) { Text("Delete") }
                }
            )
            HorizontalDivider()
        }
    }
}
