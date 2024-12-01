package com.example.safebyte.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.safebyte.R
import com.example.safebyte.ui.components.IconType
import com.example.safebyte.ui.components.SBTextField

@Composable
fun LoginScreen() {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
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
                    text = "Safe Byte",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp, 0.dp)
                        .fillMaxWidth()
                ) {
                    Text("Bem vindo!")

                    SBTextField(
                        icon = IconType.ResourceIcon(R.drawable.fa_envelope),
                        placeholder = "Email",
                        modifier = Modifier,
                        onTextChange = {}
                    )

                    SBTextField(
                        icon = IconType.ResourceIcon(R.drawable.fa_eye),
                        placeholder = "Password",
                        modifier = Modifier,
                        onTextChange = {}
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