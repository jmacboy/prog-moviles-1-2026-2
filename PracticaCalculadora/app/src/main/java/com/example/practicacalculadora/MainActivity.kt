package com.example.practicacalculadora

import android.os.Bundle
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    //state hoisting
    var result by rememberSaveable { mutableStateOf("") }
    var operation by rememberSaveable { mutableIntStateOf(0) }
    var firstNumber by rememberSaveable { mutableIntStateOf(0) }
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = result.ifEmpty { "0" },
            textAlign = TextAlign.End,
            fontSize = 48.sp,
            modifier = Modifier.fillMaxWidth()
        )
        NumberPanel(
            onNumberClick = { number ->
                if (result == "Error") {
                    result = ""
                }
                result += number
            }
        )
        OperationsPanel(
            startOperation = { currentOperation ->
                operation = currentOperation
                firstNumber = result.ifEmpty { "0" }.toInt()
                result = ""

            },
            doOperation = {
                val secondNumber = result.ifEmpty { "0" }.toInt()
                result = when (operation) {
                    0 -> (firstNumber + secondNumber).toString()
                    1 -> (firstNumber - secondNumber).toString()
                    2 -> (firstNumber * secondNumber).toString()
                    3 -> if (secondNumber != 0) {
                        (firstNumber / secondNumber).toString()
                    } else {
                        "Error"
                    }

                    else -> result
                }
            }
        )
        CleanupPanel(
            onCleanEverythingClick = {
                result = ""
                operation = 0
            },
            onCleanOneClick = {
                if (result.isNotEmpty()) {
                    result = result.dropLast(1)
                }
            }
        )
        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
fun CleanupPanel(
    onCleanEverythingClick: () -> Unit,
    onCleanOneClick: () -> Unit
) {
    Row {
        Button(onClick = {
            onCleanEverythingClick()
        }, modifier = Modifier.weight(1f)) { Text("CE") }
        Button(onClick = {
            onCleanOneClick()
        }, modifier = Modifier.weight(1f)) { Text("C") }
    }
}

@Composable
fun OperationsPanel(startOperation: (Int) -> Unit, doOperation: () -> Unit) {
    Row {
        Button(onClick = {
            startOperation(0)
        }, modifier = Modifier.weight(1f)) { Text("+") }
        Button(onClick = {
            startOperation(1)
        }, modifier = Modifier.weight(1f)) { Text("-") }
        Button(onClick = {
            startOperation(2)
        }, modifier = Modifier.weight(1f)) { Text("x") }
        Button(onClick = {
            startOperation(3)
        }, modifier = Modifier.weight(1f)) { Text("/") }
    }
    Button(onClick = { doOperation() }, modifier = Modifier.fillMaxWidth()) { Text("=") }

}

@Composable
fun NumberPanel(onNumberClick: (String) -> Unit) {
    Row {
        Button(onClick = {
            onNumberClick("1")
        }, modifier = Modifier.weight(1f)) { Text("1") }
        Button(onClick = {
            onNumberClick("2")
        }, modifier = Modifier.weight(1f)) { Text("2") }
        Button(onClick = {
            onNumberClick("3")
        }, modifier = Modifier.weight(1f)) { Text("3") }
    }
    Row {
        Button(onClick = {
            onNumberClick("4")
        }, modifier = Modifier.weight(1f)) { Text("4") }
        Button(onClick = {
            onNumberClick("5")
        }, modifier = Modifier.weight(1f)) { Text("5") }
        Button(onClick = {
            onNumberClick("6")
        }, modifier = Modifier.weight(1f)) { Text("6") }
    }
    Row {
        Button(onClick = {
            onNumberClick("7")
        }, modifier = Modifier.weight(1f)) { Text("7") }
        Button(onClick = {
            onNumberClick("8")
        }, modifier = Modifier.weight(1f)) { Text("8") }
        Button(onClick = {
            onNumberClick("9")
        }, modifier = Modifier.weight(1f)) { Text("9") }
    }
    Button(onClick = {
        onNumberClick("0")
    }, modifier = Modifier.fillMaxWidth()) { Text("0") }
}

@Preview(showBackground = true)
@Composable
fun CalculadoraPreview() {
    PracticaCalculadoraTheme {
        Calculadora()
    }
}