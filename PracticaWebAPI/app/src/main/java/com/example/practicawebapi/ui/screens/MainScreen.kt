package com.example.practicawebapi.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicawebapi.api.models.Post

@Composable
fun MainScreen(
    onPostClick: (Int) -> Unit = {},
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        PostList(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel,
            onPostClick = onPostClick
        )
    }
}

@Composable
fun PostList(
    modifier: Modifier,
    viewModel: MainViewModel,
    onPostClick: (Int) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getPosts()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.value.error != null) {
            Text(
                text = uiState.value.error ?: "Error desconocido",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        } else if (uiState.value.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.value.posts.isNotEmpty()) {
            LazyColumn(modifier) {
                items(uiState.value.posts) { post ->
                    PostItem(
                        post = post,
                        onClick = { onPostClick(post.id) }
                    )
                    HorizontalDivider()
                }
            }
        } else {
            Text(
                text = "No hay posts disponibles",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun PostItem(
    post: Post,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(text = "ID: ${post.id}", style = MaterialTheme.typography.labelSmall)
        Text(text = post.title, style = MaterialTheme.typography.titleMedium)
    }
}
