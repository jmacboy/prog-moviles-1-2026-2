package com.example.practicalistas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practicalistas.ui.theme.PracticaListasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticaListasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListExample(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ListExample(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        NamesList(modifier = Modifier.weight(1f))
        ItemList(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ItemList(modifier: Modifier = Modifier) {
    val items = listOf(
        "Item 1",
        "Item 2",
        "Item 3",
        "Item 4",
        "Item 5",
        "Item 6",
        "Item 7",
        "Item 8",
        "Item 9",
        "Item 10"
    )
    LazyRow(modifier = modifier) {
        items(items) { item ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(1.dp)
                    .background(Color.Blue)
                    .fillMaxHeight()
                    .padding(16.dp)
            ){
                Text(
                    text = item,
                    textAlign = TextAlign.Center,

                )
            }
        }
    }
}


@Composable
fun NamesList(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(100) {
            Text(
                text = "Nombre $it", modifier = Modifier
                    .padding(1.dp)
                    .background(Color.Red)
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NameListPreview() {
    PracticaListasTheme {
        NamesList()
    }
}
