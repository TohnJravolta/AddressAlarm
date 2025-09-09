package org.flagdrive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api // Added for OptIn
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue // Added
import androidx.compose.runtime.setValue // Added
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.flagdrive.data.FlaggedPlace

/**
 * Simple editor dialog for creating or updating a FlaggedPlace.
 *
 * @param initial if not null, fields are prefilled for editing.
 * @param onDismiss close the dialog without saving.
 * @param onSave (name, address, tagsCsv, notes) -> Unit
 */
@OptIn(ExperimentalMaterial3Api::class) // Added OptIn as global compiler arg was removed
@Composable
fun PlaceEditorDialog(
    initial: FlaggedPlace? = null,
    onDismiss: () -> Unit,
    onSave: (name: String, address: String, tagsCsv: String, notes: String) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue(initial?.name ?: "")) }
    var address by remember { mutableStateOf(TextFieldValue(initial?.rawAddress ?: "")) }
    var tags by remember { mutableStateOf(TextFieldValue(initial?.tags?.joinToString(", ") ?: "")) }
    var notes by remember { mutableStateOf(TextFieldValue(initial?.notes ?: "")) }

    val editing = initial != null
    val canSave = address.text.trim().isNotEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (editing) "Edit entry" else "Add entry") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address (required)") },
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        if (!canSave) Text("Address cannot be empty")
                    },
                    isError = !canSave
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        name.text,
                        address.text,
                        tags.text,
                        notes.text
                    )
                },
                enabled = canSave
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}