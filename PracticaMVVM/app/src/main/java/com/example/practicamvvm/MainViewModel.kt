package com.example.practicamvvm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateUsername(name: String) {
        _uiState.update {
            it.copy(username = name)
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun login() {
        val username = _uiState.value.username
        val password = _uiState.value.password
        if (username == "admin" && password == "admin") {
            _uiState.update {
                it.copy(
                    showSuccessMessage = true,
                    showErrorMessage = false
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    showErrorMessage = true,
                    showSuccessMessage = false
                )
            }
        }
    }
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val showSuccessMessage: Boolean = false,
    val showErrorMessage: Boolean = false
)