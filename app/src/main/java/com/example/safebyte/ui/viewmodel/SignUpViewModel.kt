package com.example.safebyte.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebyte.ui.viewmodel.validations.ValidateEmail
import com.example.safebyte.ui.viewmodel.validations.ValidatePassword
import com.example.safebyte.ui.viewmodel.validations.ValidateFullName
import com.example.safebyte.ui.viewmodel.validations.ValidatePasswordMatch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class SignUpUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val isLoading: Boolean = false,
    val isRegistered: Boolean = false,

    val error: String? = null,

    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)

class SignUpViewModel(
    private val validateFullName: ValidateFullName = ValidateFullName(),
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validatePasswordMatch: ValidatePasswordMatch = ValidatePasswordMatch()
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> get() = _uiState

    private val validateEventChannel = Channel<ValidationEvent>()
    val validateEvents = validateEventChannel.receiveAsFlow()

    fun updateFullName(name: String) {
        _uiState.value = _uiState.value.copy(
            fullName = name,
            fullNameError = null
        )
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null
        )
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null,
            confirmPasswordError = null
        )
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = null
        )
    }

    private fun validateForm(): Boolean {
        val fullNameResult = validateFullName.execute(_uiState.value.fullName)
        val emailResult = validateEmail.execute(_uiState.value.email)
        val passwordResult = validatePassword.execute(_uiState.value.password)
        val passwordMatchResult = validatePasswordMatch.execute(
            _uiState.value.password,
            _uiState.value.confirmPassword
        )

        val hasError = listOf(
            fullNameResult,
            emailResult,
            passwordResult,
            passwordMatchResult
        ).any { !it.successful }

        if (hasError) {
            _uiState.value = _uiState.value.copy(
                fullNameError = fullNameResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                confirmPasswordError = passwordMatchResult.errorMessage
            )
        }

        return !hasError
    }

    fun signUp(onSuccess: () -> Unit) {
        if(!validateForm()) return

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                // Simulate API call
                kotlinx.coroutines.delay(2000)

                _uiState.value = _uiState.value.copy(
                    isRegistered = true,
                    isLoading = false
                )

                validateEventChannel.send(ValidationEvent.Success)

                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)

                when {
                    e.message?.contains("400") == true -> {
                        validateEventChannel.send(ValidationEvent.Error("Dados inválidos!"))
                    }
                    e.message?.contains("409") == true -> {
                        validateEventChannel.send(ValidationEvent.Error("Email já cadastrado!"))
                    }
                    else -> {
                        validateEventChannel.send(ValidationEvent.Error("Não foi possível realizar o cadastro!"))
                    }
                }
            }
        }
    }

    sealed class ValidationEvent {
        data object Success: ValidationEvent()
        data class Error(val message: String): ValidationEvent()
    }
}