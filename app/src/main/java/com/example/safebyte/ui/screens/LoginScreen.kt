package com.example.safebyte.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.safebyte.R
import com.example.safebyte.ui.components.IconType
import com.example.safebyte.ui.components.SBButtonPrimary
import com.example.safebyte.ui.components.SBPasswordField
import com.example.safebyte.ui.components.SBTextField
import com.example.safebyte.viewmodel.LoginViewModel
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}) {
    val viewModel = LoginViewModel()
    val uiState = viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color(0xFFEFF7FF)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Logo and title
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo do Safe Byte",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Text(
                    text = "Safe Byte",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                // Login form
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Bem vindo!")

                    // Email field with validation
                    SBTextField(
                        icon = IconType.ResourceIcon(R.drawable.fa_envelope),
                        placeholder = "Email",
                        modifier = Modifier,
                        value = uiState.value.email,
                        onTextChange = {
                            viewModel.updateEmail(it)
                        },
                        onFocusChange = {
                            if (it) viewModel.onFieldTouched("email")
                        }
                    )
                    if (uiState.value.touchedFields.contains("email")) {
                        uiState.value.fieldErrors["email"]?.forEach { error ->
                            Text(
                                text = error,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    // Password field with validation
                    SBPasswordField(
                        placeholder = "Password",
                        value = uiState.value.password,
                        modifier = Modifier,
                        onTextChange = {
                            viewModel.updatePassword(it)
                        },
                        onFocusChange = {
                            if (it) viewModel.onFieldTouched("password")
                        }
                    )
                    if (uiState.value.touchedFields.contains("password")) {
                        uiState.value.fieldErrors["password"]?.forEach { error ->
                            Text(
                                text = error,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    // Login button
                    SBButtonPrimary(
                        label = "Entrar",
                        onClick = {
                            viewModel.validateEmail(uiState.value.email)
                            viewModel.validatePassword(uiState.value.password)
                            if (uiState.value.fieldErrors.values.all { it.isEmpty() }) {
                                viewModel.login(onSuccess = onLoginSuccess)
                            }
                        },
                        isLoading = uiState.value.isLoading
                    )
                }

                // Social login options
                Text("Ou entre com")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo_google),
                        contentDescription = "Google",
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Fit
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_logo_linkedin),
                        contentDescription = "Linkedin",
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Fit
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_logo_ig),
                        contentDescription = "Instagram",
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // Sign-up suggestion
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Primeira vez?")
                    Text(
                        text = "Criar conta",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
