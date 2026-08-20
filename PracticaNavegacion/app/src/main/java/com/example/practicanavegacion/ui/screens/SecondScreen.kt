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
fun SecondScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        SecondScreenContent(
            Modifier.padding(innerPadding),
            navController
        )
    }
}

@Composable
fun SecondScreenContent(modifier: Modifier, navController: NavHostController) {
    Column(modifier = modifier) {
        Text(
            modifier = modifier,
            text = "Soy la 2da pantalla"
        )
        Button(
            onClick = {
                navController.navigate("third")
            }
        ) {
            Text("Ir a la 3ra pantalla")
        }
    }
}

@Preview
@Composable
fun SecondScreenPreview() {
    PracticaNavegacionTheme {
        SecondScreen()
    }
}