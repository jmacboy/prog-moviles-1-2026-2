package com.example.plantillaexamen1

import androidx.lifecycle.ViewModel
import com.example.plantillaexamen1.ui.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MyCoffeeUiState())
    val uiState: StateFlow<MyCoffeeUiState> = _uiState.asStateFlow()

    fun setDrinkName(name: String) {
        _uiState.update {
            it.copy(drinkName = name)
        }
    }

    fun addToOrder() {
        if (_uiState.value.drinkName.isBlank()) {
            _uiState.update { it.copy(showDrinkError = true) }
            return
        }

        if (_uiState.value.selectedSize.isBlank()) {
            _uiState.update { it.copy(showSizeError = true) }
            return
        }
        _uiState.update {
            it.copy(
                showDrinkError = false,
                showSizeError = false,
                currentOrder = Order(
                    name = _uiState.value.drinkName,
                    size = _uiState.value.selectedSize,
                    milkType = _uiState.value.selectedMilk,
                    extraCinnamon = _uiState.value.canelaSelected,
                    extraChocolate = _uiState.value.chocolateSelected,
                    extraMilk = _uiState.value.lecheExtraSelected
                )
            )
        }
    }

    fun setSelectedSize(size: String) {
        _uiState.update {
            it.copy(selectedSize = size)
        }
    }

    fun toggleCanela() {
        _uiState.update {
            it.copy(canelaSelected = !it.canelaSelected)
        }
    }

    fun toggleChocolate() {
        _uiState.update {
            it.copy(chocolateSelected = !it.chocolateSelected)
        }
    }

    fun toggleLecheExtra() {
        _uiState.update {
            it.copy(lecheExtraSelected = !it.lecheExtraSelected)
        }
    }

    fun setSelectedMilk(milk: String) {
        _uiState.update {
            it.copy(selectedMilk = milk)
        }
    }

    fun cleanOrder() {
        _uiState.update {
            it.copy(
                drinkName = "",
                selectedSize = "",
                canelaSelected = false,
                chocolateSelected = false,
                lecheExtraSelected = false,
                selectedMilk = "Normal",
                currentOrder = null,
                showSizeError = false,
                showDrinkError = false
            )
        }
    }

}

data class MyCoffeeUiState(
    val drinkName: String = "",
    val selectedSize: String = "",
    val canelaSelected: Boolean = false,
    val chocolateSelected: Boolean = false,
    val lecheExtraSelected: Boolean = false,
    val selectedMilk: String = "Normal",
    val currentOrder: Order? = null,
    val showSizeError: Boolean = false,
    val showDrinkError: Boolean = false
)