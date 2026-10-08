package com.example.practicalistanavegacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practicalistanavegacion.ui.screens.FormScreen
import com.example.practicalistanavegacion.ui.screens.ListScreen
import com.example.practicalistanavegacion.ui.theme.PracticaListaNavegacionTheme
import com.example.practicalistanavegacion.ui.vms.ContactListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaListaNavegacionTheme {
                NavigationApp()
            }
        }
    }
}

@Composable
private fun NavigationApp(
    navController: NavHostController = rememberNavController(),
    viewModel: ContactListViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            ListScreen(navController = navController, vm = viewModel)
        }
        composable("second") {
            FormScreen(navController = navController, vm = viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticaListaNavegacionTheme {
        NavigationApp()
    }
}