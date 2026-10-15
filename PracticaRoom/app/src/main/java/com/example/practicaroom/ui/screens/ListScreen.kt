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
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.practicaroom.db.entities.Contact
import com.example.practicaroom.ui.vms.ContactListViewModel

@Composable
fun ListScreen(
    navController: NavHostController,
    vm: ContactListViewModel = viewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                vm.resetSelectedContact()
//                vm.insertContact("Juan Perez","123456789")
                navController.navigate("second")
            }) {
                Text("+")
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
    vm: ContactListViewModel = viewModel()
) {
    val uiState = vm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        vm.loadContacts()
    }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        item() {
            OutlinedTextField(
                value = uiState.value.searchText,
                onValueChange = { vm.setSearchText(it) },
                label = { Text("Buscar contacto") },
                leadingIcon = { Text("🔍") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
        items(uiState.value.contactList) { contact ->
            ContactItem(
                contact = contact,
                onItemSelected = {
                    vm.selectContact(it)
                    navController.navigate("second")
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
    contact: Contact,
    onItemRemoved: (Contact) -> Unit,
    onItemSelected: (Contact) -> Unit,
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
                text = contact.phoneNumber,
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
        contact = Contact(1, "John Doe", "123-456-7890"),
        onItemRemoved = {},
        onItemSelected = {}
    )
}