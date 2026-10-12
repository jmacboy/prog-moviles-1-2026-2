package com.example.practicaroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practicaroom.repositories.ContactRepository
import com.example.practicaroom.ui.screens.ListScreen
import com.example.practicaroom.ui.theme.PracticaRoomTheme
import com.example.practicaroom.ui.vms.ContactListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaRoomTheme {
                NavigationApp()
            }
        }
    }
}

@Composable
private fun NavigationApp(
    navController: NavHostController = rememberNavController(),
    viewModel: ContactListViewModel = ContactListViewModel(
        ContactRepository(LocalContext.current)
    )
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            ListScreen(navController = navController, vm = viewModel)
        }
    }
}