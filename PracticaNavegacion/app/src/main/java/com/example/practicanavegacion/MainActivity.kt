package com.example.practicanavegacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practicanavegacion.ui.screens.HomeScreen
import com.example.practicanavegacion.ui.screens.SecondScreen
import com.example.practicanavegacion.ui.screens.ThirdScreen
import com.example.practicanavegacion.ui.theme.PracticaNavegacionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaNavegacionTheme {
               NavigationApp()
            }
        }
    }
}
@Composable
private fun NavigationApp(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("second") {
            SecondScreen(navController)
        }
        composable("third") {
            ThirdScreen()
        }
    }
}
