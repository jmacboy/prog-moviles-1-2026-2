package com.example.practicalista2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicalista2.ui.theme.PracticaLista2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var showAddContactForm by mutableStateOf(false)
        setContent {
            PracticaLista2Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            showAddContactForm = true
                        }) {
                            Text("+")
                        }
                    }) { innerPadding ->
                    ContactList(
                        showAddContactForm,
                        modifier = Modifier.padding(innerPadding),
                        setShowAddContactForm = { showAddContactForm = it })
                }
            }
        }
    }
}

@Composable
fun ContactList(
    showAddContactForm: Boolean,
    modifier: Modifier = Modifier,
    setShowAddContactForm: (Boolean) -> Unit
) {
    val contacts = rememberSaveable {
        mutableStateListOf(
            Contact("John Doe", "123-456-7890"),
            Contact("Jane Smith", "987-654-3210"),
            Contact("Alice Johnson", "555-123-4567"),
            Contact("Bob Brown", "111-222-3333"),
            Contact("Charlie Davis", "444-555-6666"),
            Contact("David Evans", "777-888-9999"),
            Contact("Eve Foster", "000-111-2222"),
            Contact("Frank Green", "333-444-5555"),
            Contact("Grace Harris", "666-777-8888"),
            Contact("Hannah Ingram", "999-000-1111"),
            Contact("Ian Jackson", "222-333-4444"),
            Contact("Jackie King", "555-666-7777"),
            Contact("Kevin Lee", "888-999-0000"),
            Contact("Laura Martinez", "111-222-3333"),
            Contact("Mike Nelson", "444-555-6666"),
        )
    }
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        if (showAddContactForm) {
            ContactForm(
                onAddContact = { name, phoneNumber ->
                    contacts.add(Contact(name, phoneNumber))
                    setShowAddContactForm(false)
                }
            )
        }
        LazyColumn {
            items(contacts) { contact ->
                ContactItem(contact = contact, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun ContactForm(onAddContact: (String, String) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    Column {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (name.isNotBlank() && phoneNumber.isNotBlank()) {
                    onAddContact(name, phoneNumber)
                    name = ""
                    phoneNumber = ""
                }
            }
        ) {
            Text("Agregar contacto")
        }
    }

}

@Composable
fun ContactItem(contact: Contact, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = contact.name,
            fontSize = 10.sp,
            modifier = Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth()
        )
        Text(
            text = contact.phoneNumber, fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactFormPreview() {
    PracticaLista2Theme {
        ContactForm(onAddContact = { _, _ -> })
    }
}

@Preview(showBackground = true)
@Composable
fun ContactItemPreview() {
    PracticaLista2Theme {
        ContactItem(contact = Contact("John Doe", "123-456-7890"))
    }
}

@Preview(showBackground = true)
@Composable
fun ContactListPreview() {
    PracticaLista2Theme {
        ContactList(showAddContactForm = false, setShowAddContactForm = {})
    }
}