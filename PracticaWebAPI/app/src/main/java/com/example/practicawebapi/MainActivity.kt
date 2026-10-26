package com.example.practicawebapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.practicawebapi.navigation.AppNavHost
import com.example.practicawebapi.ui.theme.PracticaWebAPITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaWebAPITheme {
                AppNavHost()
            }
        }
    }
}