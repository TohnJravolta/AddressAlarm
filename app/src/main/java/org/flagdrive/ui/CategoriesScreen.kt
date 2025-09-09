package org.flagdrive.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api // Added import
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class) // Added OptIn annotation
@Composable
fun CategoriesScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Categories / Tags / Flags") }) }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Coming soon:\n• Manage Tags\n• Manage Flags\n• Groups & Areas",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "This page will let you create/rename/delete categorizations used across entries.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}