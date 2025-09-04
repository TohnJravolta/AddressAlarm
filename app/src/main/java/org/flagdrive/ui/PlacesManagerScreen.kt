package org.flagdrive.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesManagerScreen(
    vm: PlacesViewModel
) {
    val places = vm.places.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FlagDrive places") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.addSample() }) {
                Text("+")
            }
        }
    ) { inner ->
        PlacesList(
            places = places,
            onDelete = { vm.delete(it.id) },        // <- pass id Long
            modifier = Modifier
                .padding(inner)                     // <- use Scaffold's content padding
                .fillMaxSize()
        )
    }
}

/**
 * Very small placeholder list so we compile.
 * Replace with your real list composable if you already have one.
 */
@Composable
fun PlacesList(
    places: List<org.flagdrive.data.FlaggedPlace>,
    onDelete: (org.flagdrive.data.FlaggedPlace) -> Unit,
    modifier: Modifier = Modifier
) {
    // Minimal stand-in UI so builds succeed
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
            Divider()
        }
    }
}
