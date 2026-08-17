package com.example.practicaintents

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicaintents.ui.theme.PracticaIntentsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaIntentsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context: Context = LocalContext.current
    Column(
        modifier = modifier
    ) {
        Text("Hola soy el main activity")
        Button(
            onClick = {
                val intent = Intent(context, SegundoActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text("Ir al 2do activity")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PracticaIntentsTheme {
        MainScreen()
    }
}