package com.example.practicaroom.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.practicaroom.ui.vms.ContactListViewModel
import com.example.practicaroom.ui.vms.ContactUi

@Composable
fun ListScreen(
    navController: NavHostController,
    vm: ContactListViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("second/0")
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar contacto"
                )
            }
        }) { innerPadding ->
        ContactList(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            vm = vm
        )
    }
}

@Composable
fun ContactList(
    modifier: Modifier,
    navController: NavHostController,
    vm: ContactListViewModel = hiltViewModel()
) {
    val uiState = vm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        vm.loadContacts()
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            OutlinedTextField(
                value = uiState.value.searchText,
                onValueChange = { vm.setSearchText(it) },
                label = { Text("Buscar contacto") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
        items(uiState.value.contactList) { contact ->
            ContactItem(
                contact = contact,
                onItemSelected = { selectedContact ->
                    navController.navigate("second/${selectedContact.id}")
                },
                onItemRemoved = {
                    vm.deleteContact(it)
                },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ContactItem(
    contact: ContactUi,
    onItemRemoved: (ContactUi) -> Unit,
    onItemSelected: (ContactUi) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentContext = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemSelected(contact) }
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onItemSelected(contact) }
        ) {
            Text(
                text = contact.name,
                fontSize = 10.sp,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .fillMaxWidth()

            )
            Text(
                text = contact.phoneNumbers.joinToString(" · "),
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Button(
            onClick = {
                onItemRemoved(contact)
                Toast.makeText(
                    currentContext,
                    "Contacto eliminado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier
                .padding(start = 8.dp)
        ) {
            Text("Eliminar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactItemPreview() {
    ContactItem(
        contact = ContactUi(1, "John Doe", phoneNumbers = listOf("123-456-7890")),
        onItemRemoved = {},
        onItemSelected = {}
    )
}
