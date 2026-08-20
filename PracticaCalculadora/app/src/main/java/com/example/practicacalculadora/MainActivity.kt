package com.example.practicacalculadora

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicacalculadora.ui.theme.PracticaCalculadoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaCalculadoraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Calculadora(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Calculadora(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text("0", textAlign = TextAlign.End,
            fontSize = 48.sp,
            modifier = Modifier.fillMaxWidth())
        Row {
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("1") }
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("2") }
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("3") }
        }
        Row {
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("4") }
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("5") }
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("6") }
        }
        Row {
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("7") }
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("8") }
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("9") }
        }
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("0") }
        Row {
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("+") }
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("-") }
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("x") }
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("/") }
        }
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("=") }
        Row {
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("CE") }
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("C") }
        }
        Spacer(modifier = Modifier.weight(1f))

    }
}

@Preview(showBackground = true)
@Composable
fun CalculadoraPreview() {
    PracticaCalculadoraTheme {
        Calculadora()
    }
}