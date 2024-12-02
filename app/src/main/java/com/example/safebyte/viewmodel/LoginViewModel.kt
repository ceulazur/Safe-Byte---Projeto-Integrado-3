package com.example.safebyte.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val fieldErrors: Map<String, List<String>> = emptyMap(),
    val touchedFields: Set<String> = emptySet()
)


class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> get() = _uiState

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
        validateEmail(email)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
        validatePassword(password)
    }

    fun onFieldTouched(fieldName: String) {
        _uiState.value = _uiState.value.copy(
            touchedFields = _uiState.value.touchedFields + fieldName
        )
    }

    private fun validateEmail(email: String) {
        val errors = mutableListOf<String>()
        if (email.isEmpty()) {
            errors.add("O email é obrigatório")
        }
        if (email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors.add("Digite um email válido")
        }
        updateFieldErrors("email", errors)
    }

    private fun validatePassword(password: String) {
        val errors = mutableListOf<String>()
        if (password.isEmpty()) {
            errors.add("A senha é obrigatória")
        }
        if (password.isNotEmpty() && password.length < 8) {
            errors.add("A senha precisa ter no mínimo 8 caracteres")
        }
        updateFieldErrors("password", errors)
    }

    private fun updateFieldErrors(fieldName: String, errors: List<String>) {
        _uiState.value = _uiState.value.copy(
            fieldErrors = _uiState.value.fieldErrors.toMutableMap().apply {
                put(fieldName, errors)
            }
        )
    }
}
