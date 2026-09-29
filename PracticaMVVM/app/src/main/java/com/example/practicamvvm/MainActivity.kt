package com.example.practicamvvm

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicamvvm.ui.theme.PracticaMVVMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaMVVMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaLogin(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaLogin(modifier: Modifier = Modifier, vm: MainViewModel = viewModel()) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.showErrorMessage) {
        if (uiState.showErrorMessage) {
            Toast.makeText(
                context,
                "Usuario o contraseña incorrectos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    LaunchedEffect(uiState.showSuccessMessage) {
        if (uiState.showSuccessMessage) {
            Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT)
                .show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Iniciar sesión", modifier = Modifier.padding(bottom = 16.dp))
        OutlinedTextField(
            value = uiState.username,
            onValueChange = {
                vm.updateUsername(it)
            },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.password,
            onValueChange = {
                vm.updatePassword(it)
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.padding(top = 16.dp)) {
            Button(
                onClick = {
                    vm.login()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Iniciar sesión")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticaMVVMTheme {
        Greeting("Android")
    }
}