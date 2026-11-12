package com.example.practicaapilibros.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaapilibros.api.models.BookList
import com.example.practicaapilibros.repositories.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface BookListUiState {
    data object Loading : BookListUiState
    data object Empty : BookListUiState
    data class Success(val books: BookList) : BookListUiState
    data class Error(val message: String) : BookListUiState
}

class BookListViewModel(
    private val repository: BookRepository = BookRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookListUiState>(BookListUiState.Loading)
    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    // One-off message for errors that should not replace the list (e.g. a failed delete).
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    // True while a pull-to-refresh reload is in progress.
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadBooks()
    }

    fun onMessageShown() {
        _message.value = null
    }

    /**
     * Deletes the book on the API and removes only that item from the current list,
     * without showing Loading, so the list stays composed and animates the removal.
     */
    fun deleteBook(bookId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteBook(bookId)
                val current = (_uiState.value as? BookListUiState.Success)?.books ?: return@launch
                val updated = BookList(current).apply { removeAll { it.id == bookId } }
                _uiState.value = if (updated.isEmpty()) {
                    BookListUiState.Empty
                } else {
                    BookListUiState.Success(updated)
                }
            } catch (e: Exception) {
                _message.value = "Error al eliminar el libro. Intente nuevamente"
                e.printStackTrace()
            }
        }
    }

    /**
     * Fetches only the saved book and inserts it, or replaces it when it already
     * exists, without showing Loading, so the list stays composed and animates the change.
     */
    fun onBookSaved(bookId: Int) {
        viewModelScope.launch {
            try {
                val book = repository.getBook(bookId)
                val current = (_uiState.value as? BookListUiState.Success)?.books ?: BookList()
                val index = current.indexOfFirst { it.id == book.id }
                val updated = BookList(current).apply {
                    if (index >= 0) set(index, book) else add(book)
                }
                _uiState.value = BookListUiState.Success(updated)
            } catch (e: Exception) {
                // If fetching the single book fails, fall back to a full reload.
                e.printStackTrace()
                loadBooks()
            }
        }
    }

    /**
     * Reloads the list from a pull-to-refresh gesture. Keeps the current content
     * on screen instead of showing Loading; on failure, a list already shown stays
     * and a message is displayed.
     */
    fun refreshBooks() {
        if (_isRefreshing.value) return
        _isRefreshing.value = true
        viewModelScope.launch {
            try {
                val books = repository.getBookList()
                _uiState.value = if (books.isEmpty()) {
                    BookListUiState.Empty
                } else {
                    BookListUiState.Success(books)
                }
            } catch (e: Exception) {
                if (_uiState.value is BookListUiState.Success) {
                    _message.value = "Error al actualizar los libros. Intente nuevamente"
                } else {
                    _uiState.value = BookListUiState.Error(
                        "Error al traer los libros. Intente nuevamente"
                    )
                }
                e.printStackTrace()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun loadBooks() {
        _uiState.value = BookListUiState.Loading
        viewModelScope.launch {
            try {
                val books = repository.getBookList()
                _uiState.value = if (books.isEmpty()) {
                    BookListUiState.Empty
                } else {
                    BookListUiState.Success(books)
                }
            } catch (e: Exception) {
                _uiState.value = BookListUiState.Error(
                    "Error al traer los libros. Intente nuevamente"
                )
                e.printStackTrace()
            }
        }
    }
}
