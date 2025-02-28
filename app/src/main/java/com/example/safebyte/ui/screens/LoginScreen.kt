package com.example.safebyte.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.safebyte.R
import com.example.safebyte.data.repository.AuthRepository
import com.example.safebyte.ui.components.*
import com.example.safebyte.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    navController: NavHostController
) {
    val context = LocalContext.current

    val authRepository = remember { AuthRepository(FirebaseAuth.getInstance()) }
    val viewModel = remember { LoginViewModel(authRepository) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                viewModel.loginWithGoogle(idToken)
            } else {
                Toast.makeText(context, "Erro ao obter ID Token", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Log.e("LoginScreen", "Erro ao autenticar com Google", e)
            Toast.makeText(context, "Erro ao autenticar com Google: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1072159158094-i5jjb7ocq2gm716uq0mdob9d2ik77eda.apps.googleusercontent.com")
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginViewModel.ValidationEvent.Success -> {
                    Toast.makeText(context, "Login realizado com sucesso", Toast.LENGTH_SHORT).show()
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                is LoginViewModel.ValidationEvent.Error -> {
                    Toast.makeText(context, event.message ?: "Erro desconhecido", Toast.LENGTH_SHORT).show()
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
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary
                )

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp, 0.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(stringResource(R.string.bem_vindo))

                    SBTextField(
                        icon = IconType.ResourceIcon(R.drawable.fa_envelope),
                        placeholder = stringResource(R.string.email),
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
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    SBButtonPrimary(
                        label = "Entrar",
                        onClick = { viewModel.login() },
                        isLoading = uiState.value.isLoading
                    )
                }

                Text("Ou entre com")

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo_google),
                        contentDescription = "Google",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { launcher.launch(googleSignInClient.signInIntent) },
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
                    Text("Primeira vez?")
                    Text(
                        text = "Criar conta",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate("sign_up")
                        }
                    )
                }
            }
        }
    }
}
