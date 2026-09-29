package com.example.practicacalculadoramvvm

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicacalculadoramvvm.ui.theme.PracticaCalculadoraMVVMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaCalculadoraMVVMTheme {
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
fun Calculadora(modifier: Modifier = Modifier, vm: CalculadoraViewModel = viewModel()) {
    // state hoisting
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = uiState.result.ifEmpty { "0" },
            textAlign = TextAlign.End,
            fontSize = 48.sp,
            modifier = Modifier.fillMaxWidth()
        )
        NumberPanel(vm)
        OperationsPanel(vm)
        CleanupPanel(vm)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun CleanupPanel(
    vm: CalculadoraViewModel = viewModel()
) {
    Row {
        Button(onClick = {
            vm.cleanEverything()
        }, modifier = Modifier.weight(1f)) { Text("CE") }
        Button(onClick = {
            vm.cleanOne()
        }, modifier = Modifier.weight(1f)) { Text("C") }
    }
}

@Composable
fun OperationsPanel(vm: CalculadoraViewModel = viewModel()) {
    Row {
        Button(onClick = {
            vm.startOperation(0)
        }, modifier = Modifier.weight(1f)) { Text("+") }
        Button(onClick = {
            vm.startOperation(1)
        }, modifier = Modifier.weight(1f)) { Text("-") }
        Button(onClick = {
            vm.startOperation(2)
        }, modifier = Modifier.weight(1f)) { Text("x") }
        Button(onClick = {
            vm.startOperation(3)
        }, modifier = Modifier.weight(1f)) { Text("/") }
    }
    Button(onClick = { vm.doOperation() }, modifier = Modifier.fillMaxWidth()) { Text("=") }

}

@Composable
fun NumberPanel(vm: CalculadoraViewModel = viewModel()) {
    Row {
        Button(onClick = {
            vm.onNumberClick("1")
        }, modifier = Modifier.weight(1f)) { Text("1") }
        Button(onClick = {
            vm.onNumberClick("2")
        }, modifier = Modifier.weight(1f)) { Text("2") }
        Button(onClick = {
            vm.onNumberClick("3")
        }, modifier = Modifier.weight(1f)) { Text("3") }
    }
    Row {
        Button(onClick = {
            vm.onNumberClick("4")
        }, modifier = Modifier.weight(1f)) { Text("4") }
        Button(onClick = {
            vm.onNumberClick("5")
        }, modifier = Modifier.weight(1f)) { Text("5") }
        Button(onClick = {
            vm.onNumberClick("6")
        }, modifier = Modifier.weight(1f)) { Text("6") }
    }
    Row {
        Button(onClick = {
            vm.onNumberClick("7")
        }, modifier = Modifier.weight(1f)) { Text("7") }
        Button(onClick = {
            vm.onNumberClick("8")
        }, modifier = Modifier.weight(1f)) { Text("8") }
        Button(onClick = {
            vm.onNumberClick("9")
        }, modifier = Modifier.weight(1f)) { Text("9") }
    }
    Button(onClick = {
        vm.onNumberClick("0")
    }, modifier = Modifier.fillMaxWidth()) { Text("0") }
}

@Preview(showBackground = true)
@Composable
fun CalculadoraPreview() {
    PracticaCalculadoraMVVMTheme {
        Calculadora()
    }
}