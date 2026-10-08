package com.example.practicalistanavegacion.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.practicalistanavegacion.models.Contact
import com.example.practicalistanavegacion.ui.vms.ContactListViewModel

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
        items(uiState.value.contactList) { contact ->
            ContactItem(
                contact = contact,
                onItemSelected = {
                    vm.selectContact(it)
                    navController.navigate("second")
                },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ContactItem(contact: Contact, onItemSelected: (Contact) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
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
}