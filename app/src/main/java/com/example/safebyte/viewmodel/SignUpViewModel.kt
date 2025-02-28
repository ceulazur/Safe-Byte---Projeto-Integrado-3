package com.example.safebyte.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safebyte.viewmodel.validations.ValidateEmail
import com.example.safebyte.viewmodel.validations.ValidateFullName
import com.example.safebyte.viewmodel.validations.ValidatePassword
import com.example.safebyte.viewmodel.validations.ValidatePasswordMatch
import com.google.firebase.auth.FirebaseAuth
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
    val confirmPasswordError: String? = null,
)

class SignUpViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val validateFullName: ValidateFullName = ValidateFullName(),
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validatePasswordMatch: ValidatePasswordMatch = ValidatePasswordMatch(),
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
        if (!validateForm()) return

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(_uiState.value.email, _uiState.value.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.value = _uiState.value.copy(
                            isRegistered = true,
                            isLoading = false
                        )

                        viewModelScope.launch {
                            validateEventChannel.send(ValidationEvent.Success)
                        }

                        onSuccess()
                    } else {
                        _uiState.value = _uiState.value.copy(isLoading = false)

                        val errorMessage = when (task.exception?.message) {
                            "The email address is already in use by another account." -> "Este email já está cadastrado!"
                            "The email address is badly formatted." -> "Formato de email inválido!"
                            "The given password is invalid." -> "A senha precisa ter pelo menos 6 caracteres!"
                            else -> "Erro ao criar conta. Tente novamente!"
                        }

                        viewModelScope.launch {
                            validateEventChannel.send(ValidationEvent.Error(errorMessage))
                        }
                    }
                }
        }
    }

    sealed class ValidationEvent {
        data object Success : ValidationEvent()
        data class Error(val message: String) : ValidationEvent()
    }
}
