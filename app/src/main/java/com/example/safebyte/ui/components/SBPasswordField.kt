package com.example.safebyte.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safebyte.R

@Composable
fun SBPasswordField(
    modifier: Modifier = Modifier,
    value: String = "",
    placeholder: String = "",
    onTextChange: (String) -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = { onTextChange(it) },
            singleLine = true,
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                innerTextField()
            },
            visualTransformation = if (isPasswordVisible) {
                // Show password as is
                VisualTransformation.None
            } else {
                // Mask password with dots
                PasswordVisualTransformation()
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Icon to toggle password visibility
        Icon(
            imageVector = if (isPasswordVisible)
                ImageVector.vectorResource(id = R.drawable.ph_lock)
            else
                ImageVector.vectorResource(id = R.drawable.ph_lock_simple_open),
            contentDescription = "Toggle Password Visibility",
            tint = Color.Gray,
            modifier = Modifier
                .size(24.dp)
                .clickable { isPasswordVisible = !isPasswordVisible } // Toggle visibility
        )
    }
}

@Preview
@Composable
fun SBPasswordFieldPreview() {
    var password by remember { mutableStateOf("") }

    SBPasswordField(
        value = password,
        placeholder = "Enter your password",
        onTextChange = { password = it }
    )
}
