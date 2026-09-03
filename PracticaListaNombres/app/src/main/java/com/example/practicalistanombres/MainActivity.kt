package com.example.practicalistanombres

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practicalistanombres.ui.theme.PracticaListaNombresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaListaNombresTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ListScreen(modifier: Modifier = Modifier) {
    val lstValues = remember { mutableStateListOf("") }
    var textValue by rememberSaveable { mutableStateOf("") }
    Column(modifier = modifier) {
        TextForm(
            textValue = textValue,
            onTextChange = { textValue = it },
            lstValues = lstValues
        )
        ItemList(lstValues)
    }
}

@Composable
fun TextForm(
    textValue: String,
    lstValues: SnapshotStateList<String>,
    onTextChange: (String) -> Unit
) {
    OutlinedTextField(
        value = textValue,
        onValueChange = onTextChange,
        label = { Text("Ingrese el texto") },
        modifier = Modifier.padding(16.dp)
    )
    Button(
        onClick = {
            lstValues.clear()
            val splittedText: ArrayList<String> =
                textValue.split(" ").toCollection(ArrayList())

            for (value in splittedText) {
                lstValues.add(value)
            }
        }
    ) {
        Text("Mostrar valores")
    }
}

@Composable
fun ItemList(lstValues: SnapshotStateList<String>) {
    LazyColumn {
        items(lstValues) {
            Text(text = it, modifier = Modifier.padding(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemListPreview() {
    PracticaListaNombresTheme {
        ItemList(lstValues = remember { mutableStateListOf("Item 1", "Item 2", "Item 3") })
    }
}

@Preview(showBackground = true)
@Composable
fun TextFormPreview() {
    PracticaListaNombresTheme {
        Column {
            TextForm(
                textValue = "",
                onTextChange = {},
                lstValues = remember { mutableStateListOf() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    PracticaListaNombresTheme {
        ListScreen()
    }
}