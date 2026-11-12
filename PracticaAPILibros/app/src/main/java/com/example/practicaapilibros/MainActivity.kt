package com.example.practicaapilibros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.practicaapilibros.ui.navigation.AppNavigation
import com.example.practicaapilibros.ui.theme.PracticaAPILibrosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaAPILibrosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Consuming the insets keeps nested Scaffolds/TopAppBars from
                    // applying the status bar padding a second time.
                    AppNavigation(
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    )
                }
            }
        }
    }
}
