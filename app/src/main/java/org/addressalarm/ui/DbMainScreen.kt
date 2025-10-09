package org.addressalarm.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.addressalarm.data.FlaggedPlace
import org.addressalarm.ui.nav.NavRoutes

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DbMainScreen(
    navController: NavController,
    viewModel: PlacesViewModel
) {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    val allPlaces: List<FlaggedPlace> by viewModel.places.collectAsState(initial = emptyList<FlaggedPlace>())

    var searchVisible by remember { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }
    var selectedTags by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }

    var showDataActions by remember { mutableStateOf(false) }
    var editorOpen by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<FlaggedPlace?>(null) }

    var bulkMode by remember { mutableStateOf(false) }
    val selectedIds = remember { mutableStateListOf<Long>() }

    // Single delete confirmation state
    var confirmDeleteId by remember { mutableStateOf<Long?>(null) }
    var confirmDeleteName by remember { mutableStateOf<String?>(null) }

    // Bulk delete confirmation state
    var confirmBulkDelete by remember { mutableStateOf(false) }

    // Support dialog state
    var showSupportDialog by remember { mutableStateOf(false) }

    val filtered: List<FlaggedPlace> = remember(allPlaces, searchText, selectedTags) {
        allPlaces.filter { place -> matches(place, searchText, selectedTags) }
    }

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
            HorizontalDivider()
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
                title = { Text("Database") },
                actions = {
                    val context = LocalContext.current
                    var showMenu by remember { mutableStateOf(false) }

                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                val intent = Intent(context, SettingsActivity::class.java)
                                context.startActivity(intent)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Support the developer") },
                            onClick = {
                                showSupportDialog = true
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Visit addressalarm.com") },
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://addressalarm.com"))
                                context.startActivity(intent)
                                showMenu = false
                            }
                        )
                    }
                }
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
                    value = searchText,
                    onValueChange = { searchText = it },
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
                        text = if (searchText.isBlank()) "No entries. Tap + to add." else "No matches for \"${searchText}\".",
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
                                .animateItem()
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
                                    Text(text = place.name?.ifBlank { place.rawAddress } ?: place.rawAddress, style = MaterialTheme.typography.titleMedium)
                                }
                                if (place.name != null) {
                                    Text(text = place.rawAddress, style = MaterialTheme.typography.bodyMedium)
                                }
                                if (place.tags.isNotEmpty()) {
                                    Spacer(Modifier.height(6.dp))
                                    Text(text = "Tags: ${place.tags.joinToString()}")
                                }
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
                                            confirmDeleteId = place.id
                                            confirmDeleteName = place.name?.ifBlank { place.rawAddress } ?: place.rawAddress
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item { Spacer(Modifier.height(72.dp)) } 
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
                    onClick = { confirmBulkDelete = true },
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

    // Single delete confirmation dialog
    if (confirmDeleteId != null) {
        AlertDialog(
            onDismissRequest = { confirmDeleteId = null; confirmDeleteName = null },
            title = { Text("Delete entry?") },
            text = { Text("Are you sure you want to delete \"${confirmDeleteName ?: "this entry"}\"? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    val id = confirmDeleteId
                    confirmDeleteId = null
                    confirmDeleteName = null
                    if (id != null) {
                        scope.launch { viewModel.remove(id) }
                    }
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { confirmDeleteId = null; confirmDeleteName = null }) { Text("Cancel") }
            }
        )
    }

    // Bulk delete confirmation dialog
    if (confirmBulkDelete) {
        val count = selectedIds.size
        AlertDialog(
            onDismissRequest = { confirmBulkDelete = false },
            title = { Text("Delete $count selected?") },
            text = { Text("Are you sure you want to delete $count selected entr${if (count == 1) "y" else "ies"}? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmBulkDelete = false
                    scope.launch {
                        allPlaces.filter { selectedIds.contains(it.id) }.forEach { viewModel.remove(it.id) }
                        selectedIds.clear()
                        bulkMode = false
                        Toast.makeText(ctx, "Deleted", Toast.LENGTH_SHORT).show()
                    }
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { confirmBulkDelete = false }) { Text("Cancel") }
            }
        )
    }

    // Support dialog with Ko-fi link
    if (showSupportDialog) {
        AlertDialog(
            onDismissRequest = { showSupportDialog = false },
            title = { Text("Support the Developer") },
            text = { Text("AddressAlarm is built and maintained by a solo developer. If it helps you, consider supporting ongoing development and hosting costs. Thank you for your good-faith support!") },
            confirmButton = {
                TextButton(onClick = {
                    showSupportDialog = false
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ko-fi.com/tohnjburray"))
                    ctx.startActivity(intent)
                }) { Text("Open Ko-fi") }
            },
            dismissButton = {
                TextButton(onClick = { showSupportDialog = false }) { Text("Close") }
            }
        )
    }
}

private fun matches(
    place: FlaggedPlace,
    query: String,
    tags: List<String>
): Boolean {
    val q = query.trim().lowercase()
    val textHit = q.isBlank() || listOfNotNull(
        place.name, place.rawAddress, place.notes
    ).any { it.contains(q, ignoreCase = true) }
    val tagHit = tags.isEmpty() || place.tags.any { placeTag -> placeTag in tags }
    return textHit && tagHit
}

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
