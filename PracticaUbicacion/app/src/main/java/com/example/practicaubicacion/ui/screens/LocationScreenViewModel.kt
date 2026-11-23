package com.example.practicaubicacion.ui.screens

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LocationScreenUiState(
    val latitude: String = "-17.783151400201643",
    val longitude: String = "-63.1820880455232",
    val locationSelected: Boolean = false
)

class LocationScreenViewModel {
    private var _uiState = MutableStateFlow(LocationScreenUiState())
    val uiState: StateFlow<LocationScreenUiState> = _uiState.asStateFlow()

    fun setLocation(latitude: String, longitude: String) {
        _uiState.update {
            it.copy(
                latitude = latitude,
                longitude = longitude,
                locationSelected = true
            )
        }
    }
}