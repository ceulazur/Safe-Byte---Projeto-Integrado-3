package com.example.safebyte.viewmodel.validations

class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "A senha precisa ter pelo menos 8 caracteres"
            )
        }

        val containsLettersAndDigits =
            password.any() { it.isDigit() } && password.any() { it.isLetter() }

        if (!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = "A senha precisa ter pelo menos uma letra e um dígito"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}