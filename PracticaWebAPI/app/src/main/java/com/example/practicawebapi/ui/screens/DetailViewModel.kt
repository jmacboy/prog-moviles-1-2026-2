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

data class DetailUiState(
    val isLoading: Boolean = false,
    val post: Post? = null,
    val error: String? = null
)

class DetailViewModel(
    val repository: PostRepository = PostRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun getPostById(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState(isLoading = true)
            try {
                val post = repository.getPostById(id)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        post = post,
                        error = null
                    )
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error al traer el post: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        post = null,
                        error = "Error al traer el post. Intente nuevamente"
                    )
                }
            }
        }
    }
}
