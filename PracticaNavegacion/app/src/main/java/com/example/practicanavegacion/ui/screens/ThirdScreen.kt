package com.example.practicanavegacion.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicanavegacion.ui.theme.PracticaNavegacionTheme


@Composable
fun ThirdScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        ThirdScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ThirdScreenContent(modifier: Modifier) {
    Text(
        modifier = modifier,
        text = "Soy la 3ra pantalla"
    )
}

@Preview
@Composable
fun ThirdScreenPreview() {
    PracticaNavegacionTheme {
        ThirdScreen()
    }
}