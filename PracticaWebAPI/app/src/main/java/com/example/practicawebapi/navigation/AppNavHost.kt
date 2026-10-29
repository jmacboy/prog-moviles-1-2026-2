package com.example.practicawebapi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practicawebapi.ui.screens.DetailScreen
import com.example.practicawebapi.ui.screens.MainScreen

sealed class AppScreen(val route: String) {
    data object Empty : AppScreen("empty")
    data object Detail : AppScreen("detail/{postId}") {
        fun createRoute(postId: Int) = "detail/$postId"
    }
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
            MainScreen(
                onPostClick = { postId ->
                    navController.navigate(AppScreen.Detail.createRoute(postId))
                }
            )
        }
        composable(
            route = AppScreen.Detail.route,
            arguments = listOf(
                navArgument("postId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            DetailScreen(
                postId = postId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
