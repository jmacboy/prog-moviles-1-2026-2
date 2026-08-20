package com.example.practicanavegacion.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.practicanavegacion.ui.theme.PracticaNavegacionTheme

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        HomeScreenContent(
            Modifier.padding(innerPadding),
            navController
        )
    }
}

@Composable
fun HomeScreenContent(modifier: Modifier, navController: NavHostController) {
    Column(modifier = modifier) {
        Text("Soy la primera pantalla")
        Button(
            onClick = {
                navController.navigate("second")
            }
        ) {
            Text("Ir a la 2da pantalla")
        }
    }
}
@Composable
@Preview
fun HomeScreenPreview(){
    PracticaNavegacionTheme {
        HomeScreen()
    }
}