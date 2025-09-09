package org.flagdrive.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue // Added
import androidx.compose.runtime.setValue // Added (though not strictly used by 'by' here, good practice for the file)
import androidx.compose.runtime.remember // Added (though not strictly used by 'by' here, good practice for the file)
import androidx.compose.runtime.saveable.rememberSaveable // Added (though not strictly used by 'by' here, good practice for the file)
import androidx.compose.material3.ExperimentalMaterial3Api // Added for OptIn
import androidx.compose.ui.Modifier
import org.flagdrive.data.FlaggedPlace // Ensure this is imported

@OptIn(ExperimentalMaterial3Api::class) // Added OptIn as global compiler arg was removed
@Composable
fun PlacesManagerScreen(
    vm: PlacesViewModel
) {
    // Updated to use 'by' delegate and explicit type
    val places: List<FlaggedPlace> by vm.places.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FlagDrive places") }
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
            onDelete = { place -> vm.remove(place.id) }, // Updated to use vm.remove and pass id
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class) // ListItem is M3
@Composable
fun PlacesList(
    places: List<org.flagdrive.data.FlaggedPlace>,
    onDelete: (org.flagdrive.data.FlaggedPlace) -> Unit,
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
