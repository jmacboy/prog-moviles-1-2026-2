package com.example.practicawebapi.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicawebapi.api.models.Post
import com.example.practicawebapi.repositories.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    val repository: PostRepository = PostRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState(emptyList()))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun getPosts() {
        viewModelScope.launch {
            val posts = repository.getPostList()
            _uiState.value = MainUiState(posts)
        }
    }
}

data class MainUiState(
    val posts: List<Post>
)
