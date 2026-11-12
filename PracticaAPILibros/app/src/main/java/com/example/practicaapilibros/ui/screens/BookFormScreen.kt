package com.example.practicaapilibros.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicaapilibros.R
import com.example.practicaapilibros.ui.theme.PracticaAPILibrosTheme

@Composable
fun BookFormScreen(
    onBack: () -> Unit,
    onBookSaved: (bookId: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookFormViewModel = viewModel(factory = BookFormViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.savedBookId) {
        uiState.savedBookId?.let(onBookSaved)
    }

    BookFormContent(
        uiState = uiState,
        onFormChange = viewModel::onFormChange,
        onSave = { viewModel.saveBook() },
        onBack = onBack,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookFormContent(
    uiState: BookFormUiState,
    onFormChange: (BookForm) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.isEditing) "Editar libro" else "Nuevo libro") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            BookFormFields(
                uiState = uiState,
                onFormChange = onFormChange,
                onSave = onSave,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun BookFormFields(
    uiState: BookFormUiState,
    onFormChange: (BookForm) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val form = uiState.form
    val enabled = !uiState.isSaving

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FormField(
            "Nombre",
            form.nombre,
            enabled,
            uiState.fieldErrors[BookField.NOMBRE]
        ) { onFormChange(form.copy(nombre = it)) }
        FormField(
            "Autor",
            form.autor,
            enabled,
            uiState.fieldErrors[BookField.AUTOR]
        ) { onFormChange(form.copy(autor = it)) }
        FormField(
            "Editorial",
            form.editorial,
            enabled,
            uiState.fieldErrors[BookField.EDITORIAL]
        ) { onFormChange(form.copy(editorial = it)) }
        FormField(
            "URL de la imagen", form.imagen, enabled, uiState.fieldErrors[BookField.IMAGEN],
            keyboardType = KeyboardType.Uri
        ) { onFormChange(form.copy(imagen = it)) }
        FormField(
            "ISBN", form.isbn, enabled, uiState.fieldErrors[BookField.ISBN],
            keyboardType = KeyboardType.Ascii
        ) { onFormChange(form.copy(isbn = it)) }
        FormField(
            "Sinopsis", form.sinopsis, enabled, uiState.fieldErrors[BookField.SINOPSIS],
            singleLine = false
        ) { onFormChange(form.copy(sinopsis = it)) }

        uiState.errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Button(onClick = onSave, enabled = enabled, modifier = Modifier.fillMaxWidth()) {
            if (uiState.isSaving) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Guardar")
            }
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    enabled: Boolean,
    error: String?,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        enabled = enabled,
        isError = error != null,
        supportingText = error?.let { { Text(it) } },
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 4,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun BookFormContentPreview() {
    PracticaAPILibrosTheme {
        BookFormContent(
            uiState = BookFormUiState(
                form = BookForm(
                    nombre = "Clean Code",
                    autor = "Robert C. Martin",
                    editorial = "Prentice Hall",
                    isbn = "9780132350884"
                )
            ),
            onFormChange = {},
            onSave = {},
            onBack = {}
        )
    }
}
