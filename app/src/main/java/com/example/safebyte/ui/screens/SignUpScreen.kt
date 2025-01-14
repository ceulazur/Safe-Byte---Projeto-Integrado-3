package com.example.safebyte.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.safebyte.R
import com.example.safebyte.ui.components.IconType
import com.example.safebyte.ui.components.SBButtonPrimary
import com.example.safebyte.ui.components.SBPasswordField
import com.example.safebyte.ui.components.SBTextField
import com.example.safebyte.ui.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    navigateController: NavHostController,
    onSignUpSuccess: () -> Unit = {},
) {
    val viewModel = SignUpViewModel()
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.validateEvents.collect { event ->
            when (event) {
                is SignUpViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Conta criada com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is SignUpViewModel.ValidationEvent.Error -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo do Safe Byte",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Text(
                    text = stringResource(R.string.safe_byte),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp, 0.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Criar nova conta")

                    SBTextField(
                        icon = IconType.ResourceIcon(R.drawable.ic_user),
                        placeholder = "Nome completo",
                        modifier = Modifier,
                        value = uiState.value.fullName,
                        onTextChange = { viewModel.updateFullName(it) }
                    )

                    if (uiState.value.fullNameError != null) {
                        Text(
                            text = uiState.value.fullNameError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    SBTextField(
                        icon = IconType.ResourceIcon(R.drawable.fa_envelope),
                        placeholder = "Email",
                        modifier = Modifier,
                        value = uiState.value.email,
                        onTextChange = { viewModel.updateEmail(it) }
                    )

                    if (uiState.value.emailError != null) {
                        Text(
                            text = uiState.value.emailError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    SBPasswordField(
                        placeholder = "Senha",
                        value = uiState.value.password,
                        modifier = Modifier,
                        onTextChange = { viewModel.updatePassword(it) }
                    )

                    if (uiState.value.passwordError != null) {
                        Text(
                            text = uiState.value.passwordError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    SBPasswordField(
                        placeholder = "Confirmar senha",
                        value = uiState.value.confirmPassword,
                        modifier = Modifier,
                        onTextChange = { viewModel.updateConfirmPassword(it) }
                    )

                    if (uiState.value.confirmPasswordError != null) {
                        Text(
                            text = uiState.value.confirmPasswordError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    SBButtonPrimary(
                        label = "Criar conta",
                        onClick = { viewModel.signUp(onSignUpSuccess) },
                        isLoading = uiState.value.isLoading
                    )
                }

                Text("Ou registre-se com")

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

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Já tem uma conta?")
                    Text(
                        text = "Entrar",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navigateController.navigate("login")
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val navigateController = NavHostController(LocalContext.current)
    SignUpScreen(
        navigateController
    )
}