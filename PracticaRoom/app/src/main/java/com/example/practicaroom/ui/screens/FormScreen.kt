package com.example.practicaroom.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.practicaroom.ui.theme.PracticaRoomTheme
import com.example.practicaroom.ui.vms.ContactListViewModel

@Composable
fun FormScreen(
    navController: NavHostController,
    vm: ContactListViewModel = viewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        ContactForm(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            vm = vm
        )
    }
}

@Composable
fun ContactForm(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    vm: ContactListViewModel = viewModel()
) {
    val uiState = vm.uiState.collectAsStateWithLifecycle()
    val isEditing = uiState.value.selectedContact.id != 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = uiState.value.selectedContact.name,
            onValueChange = { vm.setSelectedContactName(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.value.selectedContact.phoneNumber,
            onValueChange = { vm.setSelectedPhoneNumber(it) },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.value.selectedContact.email,
            onValueChange = { vm.setSelectedContactEmail(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiState.value.selectedContact.address,
            onValueChange = { vm.setSelectedContactAddress(it) },
            label = { Text("Dirección") },
            maxLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        Button(
            onClick = {
                vm.saveContact()
                navController.popBackStack()
            }
        ) {
            Text(if (isEditing) "Guardar contacto" else "Agregar contacto")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ContactFormPreview() {
    PracticaRoomTheme {
        ContactForm(modifier = Modifier)
    }
}