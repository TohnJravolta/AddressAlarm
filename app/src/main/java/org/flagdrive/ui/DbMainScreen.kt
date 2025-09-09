package org.flagdrive.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable // Added import
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
// Removed TextFieldValue import as it's replaced by String for searchText
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.flagdrive.data.FlaggedPlace
import org.flagdrive.ui.nav.NavRoutes

// Added OptIn for ExperimentalMaterial3Api as global opt-in via compiler arg was removed
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DbMainScreen(
    navController: NavController, // Retained NavController as onEdit was not specified for this screen
    viewModel: PlacesViewModel
) {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    // Live DB list - updated as per instruction
    val allPlaces: List<FlaggedPlace> by viewModel.places.collectAsState(initial = emptyList<FlaggedPlace>())

    // UI state - updated search and added selectedTags
    var searchVisible by remember { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") } // Changed from TextFieldValue to String
    var selectedTags by rememberSaveable { mutableStateOf<List<String>>(emptyList()) } // Added

    var showDataActions by remember { mutableStateOf(false) }
    var editorOpen by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<FlaggedPlace?>(null) }

    // Bulk delete mode
    var bulkMode by remember { mutableStateOf(false) }
    val selectedIds = remember { mutableStateListOf<Long>() } // Renamed 'selected' to 'selectedIds' for clarity

    // Filtered view - updated as per instruction
    val filtered: List<FlaggedPlace> = remember(allPlaces, searchText, selectedTags) {
        allPlaces.filter { place -> matches(place, searchText, selectedTags) }
    }

    // Bottom sheet for Data Actions (Add / Bulk Remove / Import / Export)
    if (showDataActions) {
        ModalBottomSheet(onDismissRequest = { showDataActions = false }) {
            ListItem(
                headlineContent = { Text("Add") },
                supportingContent = { Text("Create a new entry") },
                leadingContent = { Icon(Icons.Default.Add, contentDescription = null) },
                modifier = Modifier.clickable {
                    editing = null
                    editorOpen = true
                    showDataActions = false
                }
            )
            ListItem(
                headlineContent = { Text("Bulk Remove") },
                supportingContent = { Text("Select multiple entries to delete") },
                leadingContent = { Icon(Icons.Default.Delete, contentDescription = null) },
                modifier = Modifier.clickable {
                    bulkMode = true
                    selectedIds.clear()
                    showDataActions = false
                }
            )
            Divider()
            ListItem(
                headlineContent = { Text("Import (coming soon)") },
                supportingContent = { Text("CSV / JSON wizard") },
                modifier = Modifier.clickable {
                    Toast.makeText(ctx, "Import wizard coming soon", Toast.LENGTH_SHORT).show()
                    showDataActions = false
                }
            )
            ListItem(
                headlineContent = { Text("Export (coming soon)") },
                supportingContent = { Text("CSV / JSON wizard") },
                modifier = Modifier.clickable {
                    Toast.makeText(ctx, "Export wizard coming soon", Toast.LENGTH_SHORT).show()
                    showDataActions = false
                }
            )
            Spacer(Modifier.height(16.dp))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Database") }
            )
        },
        floatingActionButton = {
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                SmallFloatingActionButton(onClick = { showDataActions = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add/Remove/Import-Export")
                }
                SmallFloatingActionButton(onClick = { searchVisible = !searchVisible }) {
                    Icon(Icons.Default.Search, contentDescription = "Search/Filter")
                }
                SmallFloatingActionButton(onClick = {
                    navController.navigate(NavRoutes.CATEGORIES)
                }) {
                    Icon(Icons.Default.Category, contentDescription = "Categories")
                }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (searchVisible) {
                OutlinedTextField(
                    value = searchText, // Changed from searchQuery
                    onValueChange = { searchText = it }, // Changed from searchQuery
                    label = { Text("Search / Filter (DB-only)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    singleLine = true
                )
            }

            if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize()) {
                    Text(
                        text = if (searchText.isBlank()) "No entries. Tap + to add." else "No matches for “${searchText}”.", // Changed from searchQuery.text
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center).padding(24.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filtered, key = { it.id }) { place ->
                        val checked = remember(selectedIds, bulkMode) { selectedIds.contains(place.id) }
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItemPlacement()
                                .clickable {
                                    if (bulkMode) {
                                        if (checked) selectedIds.remove(place.id) else selectedIds.add(place.id)
                                    } else {
                                        editing = place
                                        editorOpen = true
                                    }
                                }
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (bulkMode) {
                                        Checkbox(
                                            checked = checked,
                                            onCheckedChange = { isChecked ->
                                                if (isChecked) selectedIds.add(place.id) else selectedIds.remove(place.id)
                                            }
                                        )
                                        Spacer(Modifier.width(6.dp))
                                    }
                                    // Updated text display as per instruction
                                    Text(text = place.name?.ifBlank { place.rawAddress } ?: place.rawAddress, style = MaterialTheme.typography.titleMedium)
                                }
                                // Updated text display as per instruction (rawAddress shown if name is also present)
                                if (place.name != null) {
                                    Text(text = place.rawAddress, style = MaterialTheme.typography.bodyMedium)
                                }
                                // Updated tags display as per instruction (simple Text instead of FlowRow with Chips)
                                if (place.tags.isNotEmpty()) {
                                    Spacer(Modifier.height(6.dp))
                                    Text(text = "Tags: ${place.tags.joinToString()}")
                                }
                                // Updated notes display as per instruction
                                if (!place.notes.isNullOrBlank()) {
                                    Spacer(Modifier.height(6.dp))
                                    Text(text = place.notes!!, style = MaterialTheme.typography.bodySmall)
                                }
                                if (!bulkMode) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(onClick = {
                                            // Updated ViewModel call
                                            scope.launch { viewModel.remove(place.id) }
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item { Spacer(Modifier.height(72.dp)) } // pad above FABs
                }
            }
        }
    }

    if (bulkMode) {
        BottomAppBar(
            actions = {
                Text(
                    "Selected: ${selectedIds.size}",
                    modifier = Modifier.padding(start = 16.dp)
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        scope.launch {
                            // Updated ViewModel call and usage of allPlaces
                            allPlaces.filter { selectedIds.contains(it.id) }.forEach { viewModel.remove(it.id) }
                            selectedIds.clear()
                            bulkMode = false
                            Toast.makeText(ctx, "Deleted", Toast.LENGTH_SHORT).show()
                        }
                    },
                    icon = { Icon(Icons.Default.Delete, contentDescription = null) },
                    text = { Text("Delete") }
                )
            }
        )
    }

    if (editorOpen) {
        PlaceEditorDialog(
            initial = editing,
            onDismiss = { editorOpen = false },
            onSave = { name, address, tagsCsv, notes ->
                scope.launch {
                    // Updated ViewModel call
                    viewModel.addOrUpdate(
                        rawAddress = address,
                        name = name.ifBlank { null },
                        tags = tagsCsv.split(',').mapNotNull { it.trim().ifEmpty { null } },
                        notes = notes.ifBlank { null }
                    )
                }.invokeOnCompletion { editorOpen = false }
            }
        )
    }
}

// matches function as per instruction
private fun matches(
    place: FlaggedPlace,
    query: String,
    tags: List<String>
): Boolean {
    val q = query.trim().lowercase()
    val textHit = q.isBlank() || listOfNotNull(
        place.name, place.rawAddress, place.notes
    ).any { it.contains(q, ignoreCase = true) }

    // Assuming tags filter should match if any of the place's tags are in the selectedTags list
    // Or if selectedTags is empty, it's a match (tag filter not active)
    val tagHit = tags.isEmpty() || place.tags.any { placeTag -> placeTag in tags }
    return textHit && tagHit
}

/** Simple chip flow without extra deps - Kept for now, though not used by main list item anymore */
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable () -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = horizontalArrangement) {
        content()
    }
}
