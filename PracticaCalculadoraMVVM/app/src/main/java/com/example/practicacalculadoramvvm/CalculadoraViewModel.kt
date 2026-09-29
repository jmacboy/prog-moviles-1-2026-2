package com.example.practicacalculadoramvvm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculadoraViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculadoraUiState())
    val uiState: StateFlow<CalculadoraUiState> = _uiState.asStateFlow()
    fun onNumberClick(number: String) {
        if (_uiState.value.result == "Error") {
            updateResult("")
        }
        updateResult(_uiState.value.result + number)
    }

    private fun updateResult(value: String) {
        _uiState.update {
            it.copy(result = value)
        }
    }

    fun startOperation(currentOperation: Int) {
        _uiState.update {
            it.copy(
                operation = currentOperation,
                firstNumber = it.result.ifEmpty { "0" }.toInt(),
                result = ""
            )
        }
    }

    fun doOperation() {
        val secondNumber = _uiState.value.result.ifEmpty { "0" }.toInt()
        _uiState.update {
            it.copy(
                result = when (it.operation) {
                    0 -> (it.firstNumber + secondNumber).toString()
                    1 -> (it.firstNumber - secondNumber).toString()
                    2 -> (it.firstNumber * secondNumber).toString()
                    3 -> if (secondNumber != 0) {
                        (it.firstNumber / secondNumber).toString()
                    } else {
                        "Error"
                    }

                    else -> it.result
                }
            )
        }
    }

    fun cleanEverything() {
        _uiState.update {
            it.copy(
                result = "",
                operation = 0
            )
        }
    }

    fun cleanOne() {
        if (_uiState.value.result.isNotEmpty()) {
            updateResult(_uiState.value.result.dropLast(1))
        }
    }
}

data class CalculadoraUiState(
    val result: String = "",
    val operation: Int = 0,
    val firstNumber: Int = 0
)