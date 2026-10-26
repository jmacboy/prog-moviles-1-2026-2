package com.example.practicawebapi.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practicawebapi.ui.screens.MainScreen

sealed class AppScreen(val route: String) {
    data object Empty : AppScreen("empty")
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Empty.route
    ) {
        composable(AppScreen.Empty.route) {
            MainScreen()
        }
    }
}
