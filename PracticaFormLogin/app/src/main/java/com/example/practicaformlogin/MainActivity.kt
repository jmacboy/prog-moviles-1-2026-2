package com.example.practicaformlogin

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practicaformlogin.ui.theme.PracticaFormLoginTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaFormLoginTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FormLogin(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun FormLogin(modifier: Modifier = Modifier) {
    var registro by rememberSaveable { mutableStateOf(false) }
    Column {
        Button(
            onClick = { registro = !registro },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = if (registro) "Ir a login" else "Ir a registro")
        }
        if (registro) {
            PantallaRegistro(modifier)
        } else {
            PantallaLogin(modifier)
        }
        /*
        * TAREA: En la pantalla de registro, colocar un formulario que incluya el nombre en uno de los campos.
        * Agregar el usuario registrado a una lista (una variable nomas).
        * Que de esa lista se verifique cuando se haga un login.
        */

    }
}

@Composable
fun PantallaRegistro(modifier: Modifier = Modifier) {
    Text("Pantalla de registro aqui")

}

@Composable
fun PantallaLogin(modifier: Modifier = Modifier) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Iniciar sesión", modifier = Modifier.padding(bottom = 16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.padding(top = 16.dp)) {
            Button(
                onClick = {
                    if (username == "admin" && password == "admin") {
                        Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            context,
                            "Usuario o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Iniciar sesión")
            }
        }
    }
}

@Preview
@Composable
fun FormLoginPreview() {
    PracticaFormLoginTheme {
        FormLogin()
    }
}
