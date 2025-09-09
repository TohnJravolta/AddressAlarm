package org.flagdrive.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// Removed import org.flagdrive.data.AppDatabase as it's now handled by ServiceLocator
import org.flagdrive.data.PlacesRepository
import org.flagdrive.di.ServiceLocator // Added import for ServiceLocator
import org.flagdrive.ui.nav.NavRoutes

// ViewModel Factory for PlacesViewModel - remains the same
class PlacesViewModelFactory(private val repository: PlacesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlacesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlacesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Updated to use ServiceLocator to get the repository
        val repository = ServiceLocator.repository(applicationContext)
        val viewModelFactory = PlacesViewModelFactory(repository)

        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                val nav = rememberNavController()
                // Use the custom factory to get the ViewModel instance
                val vm: PlacesViewModel = viewModel(factory = viewModelFactory)

                NavHost(navController = nav, startDestination = NavRoutes.DB_MAIN) {
                    composable(NavRoutes.DB_MAIN) {
                        DbMainScreen(navController = nav, viewModel = vm)
                    }
                    composable(NavRoutes.CATEGORIES) {
                        CategoriesScreen()
                    }
                }
            }
        }
    }
}
