package org.addressalarm.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import kotlinx.coroutines.launch
import org.addressalarm.data.PlacesRepository
import org.addressalarm.di.ServiceLocator
import org.addressalarm.ui.nav.NavRoutes

class PlacesViewModelFactory(private val repository: PlacesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlacesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlacesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private lateinit var prefs: SharedPreferences
    private val snackbarHostState = SnackbarHostState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val repository = ServiceLocator.repository(applicationContext)
        val viewModelFactory = PlacesViewModelFactory(repository)

        setContent {
            val navController = rememberNavController()
            val vm: PlacesViewModel = viewModel(factory = viewModelFactory)
            var licenseAccepted by remember { mutableStateOf(prefs.getBoolean("license_accepted", false)) }

            Surface(color = MaterialTheme.colorScheme.background) {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = { Text("AddressAlarm") },
                            actions = {
                                IconButton(onClick = {
                                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                                }) {
                                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.DB_MAIN,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(NavRoutes.DB_MAIN) {
                            DbMainScreen(navController = navController, viewModel = vm)
                        }
                        composable(NavRoutes.CATEGORIES) {
                            CategoriesScreen()
                        }
                    }

                    if (!licenseAccepted) {
                        val ctx = this@MainActivity
                        // Read LICENSE from packaged assets (copied at build time)
                        val licenseText by remember {
                            mutableStateOf(
                                runCatching {
                                    ctx.assets.open("LICENSE").bufferedReader().use { it.readText() }
                                }.getOrElse { "License file unavailable." }
                            )
                        }

                        AlertDialog(
                            onDismissRequest = { /* block dismiss to enforce explicit choice */ },
                            title = { Text("License Agreement") },
                            text = {
                                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                    Text("Please review and agree to the project's open-source license to use AddressAlarm.")
                                    Spacer(Modifier.height(12.dp))
                                    // Common-sense explanation box
                                    Card {
                                        Column(Modifier.padding(12.dp)) {
                                            Text(
                                                "Common-sense summary:",
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Spacer(Modifier.height(6.dp))
                                            Text(
                                                "You're responsible for how you use AddressAlarm. It provides reminders and information only; it never makes decisions for you. No warranty is provided; use it at your own discretion.")
                                        }
                                    }
                                    Spacer(Modifier.height(12.dp))
                                    // Full license box with bounded height and scrolling
                                    Card {
                                        Column(Modifier.padding(12.dp)) {
                                            Text(
                                                "Full License:",
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Spacer(Modifier.height(6.dp))
                                            Box(modifier = Modifier.height(320.dp).verticalScroll(rememberScrollState())) {
                                                Text(licenseText)
                                            }
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                androidx.compose.material3.TextButton(onClick = {
                                    prefs.edit().putBoolean("license_accepted", true).apply()
                                    licenseAccepted = true
                                }) { Text("I Agree") }
                            },
                            dismissButton = {
                                androidx.compose.material3.TextButton(onClick = { finish() }) { Text("Exit") }
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (prefs.getStringSet("selected_apps", emptySet())?.isEmpty() == true) {
            lifecycleScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "No apps selected for monitoring.",
                    actionLabel = "Open Settings"
                )
                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                }
            }
        }
    }
}
