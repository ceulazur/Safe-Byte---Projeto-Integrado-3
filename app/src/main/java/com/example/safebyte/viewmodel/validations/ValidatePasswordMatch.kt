package com.example.safebyte.viewmodel.validations

class ValidatePasswordMatch {
    fun execute(password: String, confirmPassword: String): ValidationResult {
        if (confirmPassword.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Confirme sua senha"
            )
        }

        if (password != confirmPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "As senhas não coincidem"
            )
        }

        return ValidationResult(successful = true)
    }
}