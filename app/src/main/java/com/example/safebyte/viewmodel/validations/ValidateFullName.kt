package com.example.safebyte.viewmodel.validations

class ValidateFullName {
    fun execute(fullName: String): ValidationResult {
        if (fullName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "O nome completo é obrigatório"
            )
        }

        if (fullName.length < 3) {
            return ValidationResult(
                successful = false,
                errorMessage = "O nome deve ter pelo menos 3 caracteres"
            )
        }

        if (!fullName.trim().contains(" ")) {
            return ValidationResult(
                successful = false,
                errorMessage = "Digite seu nome completo"
            )
        }

        return ValidationResult(successful = true)
    }
}