package com.example.practicaapilibros.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.practicaapilibros.api.models.Book
import com.example.practicaapilibros.api.models.BookRequestDTO
import com.example.practicaapilibros.repositories.BookRepository
import com.example.practicaapilibros.validation.BookValidator
import com.example.practicaapilibros.validation.IsbnType
import com.example.practicaapilibros.validation.IsbnValidation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookForm(
    val nombre: String = "",
    val autor: String = "",
    val editorial: String = "",
    val imagen: String = "",
    val sinopsis: String = "",
    val isbn: String = ""
)

enum class BookField { NOMBRE, AUTOR, EDITORIAL, IMAGEN, SINOPSIS, ISBN }

data class BookFormUiState(
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val form: BookForm = BookForm(),
    val fieldErrors: Map<BookField, String> = emptyMap(),
    val validationRequested: Boolean = false,
    val isSaving: Boolean = false,
    val savedBookId: Int? = null,
    val errorMessage: String? = null
)

class BookFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: BookRepository = BookRepository()
) : ViewModel() {

    private val bookId: Int? =
        savedStateHandle.get<Int>(BOOK_ID_ARG)?.takeIf { it != NO_BOOK_ID }

    // The form does not edit the rating, so the original value is kept on update.
    private var calificacion: Int = 0

    private val _uiState = MutableStateFlow(BookFormUiState(isEditing = bookId != null))
    val uiState: StateFlow<BookFormUiState> = _uiState.asStateFlow()

    init {
        bookId?.let { loadBook(it) }
    }

    fun loadBook(id: Int) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val book = repository.getBook(id)
                calificacion = book.calificacion
                _uiState.update { it.copy(isLoading = false, form = book.toForm()) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al cargar el libro. Intente nuevamente"
                    )
                }
                e.printStackTrace()
            }
        }
    }

    private fun Book.toForm() = BookForm(
        nombre = nombre,
        autor = autor,
        editorial = editorial,
        imagen = imagen,
        sinopsis = sinopsis,
        isbn = isbn
    )

    fun onFormChange(form: BookForm) {
        _uiState.update {
            it.copy(
                form = form,
                errorMessage = null,
                fieldErrors = if (it.validationRequested) validate(form) else emptyMap()
            )
        }
    }

    private fun validate(form: BookForm): Map<BookField, String> {
        val required = mapOf(
            BookField.NOMBRE to form.nombre,
            BookField.AUTOR to form.autor,
            BookField.EDITORIAL to form.editorial,
            BookField.IMAGEN to form.imagen,
            BookField.SINOPSIS to form.sinopsis,
            BookField.ISBN to form.isbn
        )
        val errors = required
            .filterValues { it.isBlank() }
            .mapValues { REQUIRED_MESSAGE }
            .toMutableMap()

        if (BookField.IMAGEN !in errors && !BookValidator.isValidUrl(form.imagen)) {
            errors[BookField.IMAGEN] = "URL inválida. Debe comenzar con http:// o https://"
        }
        if (BookField.ISBN !in errors) {
            isbnErrorMessage(BookValidator.validateIsbn(form.isbn))?.let {
                errors[BookField.ISBN] = it
            }
        }
        return errors
    }

    private fun isbnErrorMessage(validation: IsbnValidation): String? = when (validation) {
        IsbnValidation.Valid -> null
        is IsbnValidation.InvalidLength -> when {
            validation.length < 10 ->
                "El ISBN es muy corto: tiene ${validation.length} caracteres y debe tener 10 o 13"
            validation.length in 11..12 ->
                "El ISBN tiene ${validation.length} caracteres: debe tener 10 (ISBN-10) o 13 (ISBN-13)"
            else ->
                "El ISBN es muy largo: tiene ${validation.length} caracteres y debe tener como máximo 13"
        }
        is IsbnValidation.InvalidCharacters -> when (validation.type) {
            IsbnType.ISBN_10 -> "Un ISBN-10 solo admite dígitos y una X como último carácter"
            IsbnType.ISBN_13 -> "Un ISBN-13 solo admite dígitos"
        }
        is IsbnValidation.InvalidChecksum -> when (validation.type) {
            IsbnType.ISBN_10 -> "El dígito de control del ISBN-10 no es válido"
            IsbnType.ISBN_13 -> "El dígito de control del ISBN-13 no es válido"
        }
    }

    fun saveBook() {
        val form = _uiState.value.form
        val errors = validate(form)
        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(validationRequested = true, fieldErrors = errors) }
            return
        }
        _uiState.update { it.copy(isSaving = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val request = BookRequestDTO(
                    nombre = form.nombre.trim(),
                    autor = form.autor.trim(),
                    editorial = form.editorial.trim(),
                    imagen = form.imagen.trim(),
                    sinopsis = form.sinopsis.trim(),
                    isbn = BookValidator.normalizeIsbn(form.isbn),
                    calificacion = calificacion
                )
                val savedBook = if (bookId == null) {
                    repository.createBook(request)
                } else {
                    repository.updateBook(bookId, request)
                }
                _uiState.update { it.copy(isSaving = false, savedBookId = savedBook.id) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "Error al guardar el libro. Intente nuevamente"
                    )
                }
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val BOOK_ID_ARG = "bookId"
        const val NO_BOOK_ID = -1
        private const val REQUIRED_MESSAGE = "Campo requerido"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { BookFormViewModel(createSavedStateHandle()) }
        }
    }
}
