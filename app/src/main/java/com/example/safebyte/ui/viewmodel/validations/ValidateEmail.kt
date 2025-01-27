package com.example.safebyte.ui.viewmodel.validations

import android.util.Patterns

class ValidateEmail {
    fun execute(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "O email não pode estar em branco"
            )
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = "O email é inválido"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}