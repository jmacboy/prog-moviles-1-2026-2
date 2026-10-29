package com.example.practicawebapi.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicawebapi.api.models.Post
import com.example.practicawebapi.repositories.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    val repository: PostRepository = PostRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun getPosts() {
        viewModelScope.launch {
            _uiState.value = MainUiState(isLoading = true)
           try {
               val posts = repository.getPostList()
               _uiState.update {
                   it.copy(
                       isLoading = false,
                       posts = posts
                   )
               }
           }catch (e: Exception) {
               Log.e("MainViewModel", "Error al traer los posts: ${e.message}", e)
               _uiState.update {
                   it.copy(
                       isLoading = false,
                       error = "Error al traer los posts. Intente nuevamente"
                   )
               }
           }
        }
    }
}

data class MainUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val posts: List<Post> = emptyList()
)
