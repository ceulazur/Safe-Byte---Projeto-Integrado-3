package com.example.safebyte.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebyte.data.repository.AuthRepository
import com.example.safebyte.viewmodel.validations.ValidateEmail
import com.example.safebyte.viewmodel.validations.ValidatePassword
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
)

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> get() = _uiState

    private val eventChannel = Channel<ValidationEvent>()
    val events = eventChannel.receiveAsFlow()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    private fun validateForm(): Boolean {
        val emailResult = validateEmail.execute(_uiState.value.email)
        val passwordResult = validatePassword.execute(_uiState.value.password)

        val hasError = listOf(emailResult, passwordResult).any { !it.successful }

        _uiState.update {
            it.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
        }

        return !hasError
    }

    fun login() {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val isSuccess = authRepository.login(_uiState.value.email, _uiState.value.password)

                if (isSuccess) {
                    _uiState.update { it.copy(isLoggedIn = true, isLoading = false) }
                    eventChannel.send(ValidationEvent.Success)
                } else {
                    throw Exception("Email ou senha incorretos!")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }

                val errorMessage =
                    "Não foi possível realizar login! Por favor, verifique as credenciais informadas e tente novamente"

                eventChannel.send(ValidationEvent.Error(errorMessage))
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val isSuccess = authRepository.loginWithGoogle(credential)
                if (isSuccess) {
                    _uiState.update { it.copy(isLoggedIn = true, isLoading = false) }
                    eventChannel.send(ValidationEvent.Success)
                } else {
                    throw Exception("Erro ao autenticar com o Google")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
                eventChannel.send(ValidationEvent.Error("Falha no login com Google: ${e.message}"))
            }
        }
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Error(val message: String? = null) : ValidationEvent()
    }
}
