package com.example.safebyte.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebyte.ui.viewmodel.validations.ValidateEmail
import com.example.safebyte.ui.viewmodel.validations.ValidatePassword
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",

    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,

    val error: String? = null,

    val emailError: String? = null,
    val passwordError: String? = null
)

class LoginViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword()
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> get() = _uiState

    private val validateEventChannel = Channel<ValidationEvent>()
    val validateEvents = validateEventChannel.receiveAsFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    private fun validateForm(): Boolean {
        val emailResult = validateEmail.execute(_uiState.value.email)
        val passwordResult = validatePassword.execute(_uiState.value.password)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.successful }

        if (hasError) {
            _uiState.value = _uiState.value.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
        }

        return !hasError
    }

    fun login(onSuccess: () -> Unit) {
        if(!validateForm()) return

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                // Simulate success
                kotlinx.coroutines.delay(2000)

                validateEventChannel.send(ValidationEvent.Success)

                onSuccess()
            } catch (e: Exception) {
                if (e.message?.contains("400") == true) {
                    validateEventChannel.send(ValidationEvent.Error("Email ou senha incorretos!"))
                } else {
                    validateEventChannel.send(ValidationEvent.Error("Não foi possível realizar login!"))
                }
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    sealed class ValidationEvent {
        data object Success: ValidationEvent()
        data class Error(val message: String? = null): ValidationEvent()
    }
}