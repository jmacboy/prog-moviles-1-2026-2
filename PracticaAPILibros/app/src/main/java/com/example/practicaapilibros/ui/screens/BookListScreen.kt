package com.example.practicaapilibros.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.practicaapilibros.R
import com.example.practicaapilibros.api.models.Book
import com.example.practicaapilibros.ui.theme.PracticaAPILibrosTheme

@Composable
fun BookListScreen(
    savedBookId: Int?,
    onSavedBookHandled: () -> Unit,
    onAddClick: () -> Unit,
    onBookClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.onMessageShown()
        }
    }

    LaunchedEffect(savedBookId) {
        savedBookId?.let {
            viewModel.onBookSaved(it)
            onSavedBookHandled()
        }
    }

    BookListContent(
        uiState = uiState,
        isRefreshing = isRefreshing,
        onRefresh = viewModel::refreshBooks,
        onRetry = viewModel::loadBooks,
        onAddClick = onAddClick,
        onBookClick = onBookClick,
        onDeleteConfirm = viewModel::deleteBook,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListContent(
    uiState: BookListUiState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onAddClick: () -> Unit,
    onBookClick: (Int) -> Unit,
    onDeleteConfirm: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Book waiting for the user's confirmation; null means no dialog is shown.
    var bookToDelete by remember { mutableStateOf<Book?>(null) }

    bookToDelete?.let { book ->
        AlertDialog(
            onDismissRequest = { bookToDelete = null },
            title = { Text("Eliminar libro") },
            text = { Text("¿Está seguro de eliminar \"${book.nombre}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteConfirm(book.id)
                    bookToDelete = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { bookToDelete = null }) { Text("Cancelar") }
            }
        )
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            BookListUiState.Loading -> CircularProgressIndicator()
            // Scrollable so the pull gesture is also detected on the empty state.
            BookListUiState.Empty -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay libros disponibles")
            }
            is BookListUiState.Error -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(uiState.message)
                Spacer(Modifier.height(8.dp))
                Button(onClick = onRetry) { Text("Reintentar") }
            }

            is BookListUiState.Success -> BookList(
                books = uiState.books,
                onBookClick = onBookClick,
                onDeleteClick = { bookToDelete = it }
            )
        }
        FloatingActionButton(
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(painterResource(R.drawable.ic_add), contentDescription = "Agregar libro")
        }
    }
}

@Composable
private fun BookList(
    books: List<Book>,
    onBookClick: (Int) -> Unit,
    onDeleteClick: (Book) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(books, key = { it.id }) { book ->
            BookItem(
                book = book,
                onClick = { onBookClick(book.id) },
                onDeleteClick = { onDeleteClick(book) },
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
private fun BookItem(
    book: Book,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        // Crossfades the content when the same item (same key) changes, like
        // RecyclerView's default change animation with DiffUtil.
        AnimatedContent(
            targetState = book,
            transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(400)) },
            label = "bookItemContent"
        ) { book ->
            BookItemContent(book, onDeleteClick)
        }
    }
}

@Composable
private fun BookItemContent(book: Book, onDeleteClick: () -> Unit) {
    Row(modifier = Modifier.padding(12.dp)) {
        AsyncImage(
            model = book.imagen,
            contentDescription = book.nombre,
            placeholder = painterResource(R.drawable.ic_book_placeholder),
            error = painterResource(R.drawable.ic_book_placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 80.dp, height = 120.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = book.nombre,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = book.autor, style = MaterialTheme.typography.bodyMedium)
            Text(text = book.editorial, style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onDeleteClick) {
            Icon(painterResource(R.drawable.ic_delete), contentDescription = "Eliminar libro")
        }
    }
}

private fun sampleBook(id: Int, name: String, author: String) = Book(
    id = id,
    autor = author,
    calificacion = 4,
    editorial = "Prentice Hall",
    generos = emptyList(),
    imagen = "",
    isbn = "9780132350884",
    nombre = name,
    sinopsis = "Sample synopsis",
    created_at = "",
    updated_at = ""
)

@Preview(showBackground = true)
@Composable
private fun BookListContentPreview() {
    PracticaAPILibrosTheme {
        BookListContent(
            uiState = BookListUiState.Success(
                arrayListOf(
                    sampleBook(1, "Clean Code", "Robert C. Martin"),
                    sampleBook(2, "The Pragmatic Programmer", "Andrew Hunt"),
                    sampleBook(3, "Refactoring", "Martin Fowler")
                )
            ),
            isRefreshing = false,
            onRefresh = {},
            onRetry = {},
            onAddClick = {},
            onBookClick = {},
            onDeleteConfirm = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookItemPreview() {
    PracticaAPILibrosTheme {
        BookItem(sampleBook(1, "Clean Code", "Robert C. Martin"), onClick = {}, onDeleteClick = {})
    }
}
