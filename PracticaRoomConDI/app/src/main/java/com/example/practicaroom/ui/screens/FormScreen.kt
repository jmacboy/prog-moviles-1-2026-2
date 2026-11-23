package com.example.practicaroom.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.practicaroom.ui.theme.PracticaRoomTheme
import com.example.practicaroom.ui.vms.ContactListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    navController: NavHostController,
    contactId: Int = 0,
    vm: ContactListViewModel = hiltViewModel()
) {
    val uiState = vm.uiState.collectAsStateWithLifecycle()
    val isEditing = uiState.value.selectedContact.id != 0

    LaunchedEffect(contactId) {
        vm.loadContactById(contactId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditing) "Editar contacto" else "Nuevo contacto")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
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
    vm: ContactListViewModel = hiltViewModel()
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
        uiState.value.selectedContact.phoneNumbers.forEachIndexed { index, phone ->
            OutlinedTextField(
                value = phone,
                onValueChange = { vm.setSelectedPhoneNumber(index, it) },
                label = { Text("Teléfono ${index + 1}") },
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.value.selectedContact.phoneNumbers.size > 1) {
                Button(onClick = { vm.removePhoneNumber(index) }) {
                    Text("Quitar teléfono")
                }
            }
        }
        Button(onClick = { vm.addPhoneNumber() }) {
            Text("Agregar teléfono")
        }
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
@Preview(name = "Contact form", showBackground = true)
fun ContactFormPreview() {
    PracticaRoomTheme {
        ContactForm(modifier = Modifier)
    }
}

@Preview(name = "Form screen", showBackground = true)
@Composable
fun FormScreenPreview() {
    PracticaRoomTheme {
        FormScreen(
            navController = rememberNavController()
        )
    }
}
