package com.example.safebyte.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(
    onNavigateBack: () -> Unit,
) {
    val faqs = listOf(
        FAQ(
            "O que é o SafeByte?",
            "SafeByte é um aplicativo de gerenciamento de alergias que ajuda você a monitorar e controlar suas condições alérgicas de forma simples e eficaz."
        ),
        FAQ(
            "Como adiciono uma nova alergia?",
            "Na tela de Minhas Alergias, você pode clicar no botão '+' para adicionar uma nova alergia, inserindo detalhes como nome, sintomas e nível de gravidade."
        ),
        FAQ(
            "Posso receber notificações diárias?",
            "Sim! Na tela de Configurações, você pode ativar notificações diárias para lembretes importantes sobre suas alergias e cuidados."
        ),
        FAQ(
            "O aplicativo funciona offline?",
            "A maioria das funcionalidades do SafeByte está disponível offline. No entanto, alguns recursos como sincronização de dados podem exigir conexão à internet."
        ),
        FAQ(
            "Posso personalizar o tema do aplicativo?",
            "Com certeza! Na tela de Configurações, você pode alternar entre o tema claro e o tema escuro de acordo com sua preferência."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perguntas Frequentes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            faqs.forEach { faq ->
                ExpandableFAQCard(faq)
            }
        }
    }
}

data class FAQ(val question: String, val answer: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableFAQCard(faq: FAQ) {
    val isExpanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        onClick = { isExpanded.value = !isExpanded.value }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = rememberVectorPainter(
                        image = (
                                if (isExpanded.value)
                                    Icons.Default.KeyboardArrowDown
                                else
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight
                                )
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = faq.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            if (isExpanded.value) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = faq.answer,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}