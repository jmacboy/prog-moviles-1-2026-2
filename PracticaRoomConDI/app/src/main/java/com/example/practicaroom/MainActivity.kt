package com.example.practicaroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practicaroom.ui.screens.FormScreen
import com.example.practicaroom.ui.screens.ListScreen
import com.example.practicaroom.ui.theme.PracticaRoomTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            ListScreen(navController = navController)
        }
        composable(
            route = "second/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt("id") ?: 0
            FormScreen(
                navController = navController,
                contactId = contactId
            )
        }
    }
}