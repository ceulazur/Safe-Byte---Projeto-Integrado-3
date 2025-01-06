package com.example.safebyte.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.safebyte.R
import com.example.safebyte.ui.components.SBButtonPrimary
import com.example.safebyte.ui.components.SBTextField
import com.example.safebyte.ui.components.SecondaryTopBar
import com.example.safebyte.ui.components.Timeline
import com.example.safebyte.ui.components.TimelineEvent
import com.example.safebyte.ui.viewmodel.AllergyHistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewRecordModal(
    navController: NavController,
    allergyHistoryViewModel: AllergyHistoryViewModel,
    onDismiss: () -> Unit,
    onSaveRecord: (TimelineEvent) -> Unit
) {
    val allergyHistory by allergyHistoryViewModel.allergyHistoryState

    val newRecordDate = rememberDatePickerState()
    var newRecordVideoUrl by remember { mutableStateOf("") }
    val newRecordActivities by remember { mutableStateOf(mutableListOf<String>()) }
    var newActivityInput by remember { mutableStateOf("") }
    var showDatePickerDialog by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember {
        mutableStateOf("")
    }

    fun Long.toBrazilianDateFormat(
        pattern: String = "dd/MM/yyyy"
    ): String {
        val date = Date(this)
        val formatter = SimpleDateFormat(
            pattern, Locale("pt-br")
        ).apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }
        return formatter.format(date)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar novo registro") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Data do registro:")

                val focusManager = LocalFocusManager.current

                if (showDatePickerDialog) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePickerDialog = false },
                        confirmButton = {
                            Button(
                                onClick = {
                                    datePickerState.selectedDateMillis?.let { millis ->
                                        selectedDate = millis.toBrazilianDateFormat()
                                    }
                                    showDatePickerDialog = false
                                }
                            ) {
                                Text(text = "Escolher data")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                SBTextField(
                    value = selectedDate,
                    onTextChange = {
                        selectedDate = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusEvent {
                            if (it.isFocused) {
                                showDatePickerDialog = true
                            }
                        },
                    placeholder = "Data do registro",
                    readonly = true
                )

                // Remove o foco assim que o diálogo for exibido
                LaunchedEffect(showDatePickerDialog) {
                    if (showDatePickerDialog) {
                        focusManager.clearFocus()
                    }
                }

                Text("Link do vídeo:")

                // Video URL Input
                SBTextField(
                    value = newRecordVideoUrl,
                    onTextChange = { newRecordVideoUrl = it },
                    placeholder = "Cole o link do vídeo",
                    modifier = Modifier.fillMaxWidth()
                )

                // Activities Section
                Text("Atividades:", style = MaterialTheme.typography.titleSmall)

                // List of Current Activities
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    newRecordActivities.forEachIndexed { index, activity ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = activity,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(
                                onClick = {
                                    newRecordActivities.removeAt(index)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remover atividade"
                                )
                            }
                        }
                    }
                }

                // Add New Activity
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SBTextField(
                        value = newActivityInput,
                        onTextChange = { newActivityInput = it },
                        placeholder = "Nova Atividade",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            if (newActivityInput.isNotBlank()) {
                                newRecordActivities.add(newActivityInput)
                                newActivityInput = ""
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar atividade"
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newRecord = TimelineEvent(
                        id = allergyHistory.size + 1,
                        date = newRecordDate.displayedMonthMillis,
                        videoUrl = newRecordVideoUrl,
                        activitiesList = newRecordActivities.toList()
                    )
                    onSaveRecord(newRecord)
                    onDismiss()
                },
                enabled = newRecordVideoUrl.isNotBlank() || newRecordActivities.isNotEmpty()
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
            ) {
                Text("Cancelar")
            }
        }
    )
}


@Composable
fun AllergyHistoryScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    val allergyHistory = listOf(
        TimelineEvent(
            id = 1,
            date = Date().time,
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/aluno-d3928.appspot.com/o/profileImages%2F10240187-sd_360_640_30fps.mp4?alt=media&token=b956caff-2380-43df-9e9f-283b52219a6d",
            activitiesList = listOf(
                "Activity 1",
                "Activity 2",
                "Activity 3",
                "Activity 4",
                "Activity 5",
            )
        ), TimelineEvent(
            id = 2,
            date = Date().time,
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/aluno-d3928.appspot.com/o/profileImages%2F10240187-sd_360_640_30fps.mp4?alt=media&token=b956caff-2380-43df-9e9f-283b52219a6d",
            activitiesList = listOf(
                "Activity 1", "Activity 2", "Activity 3", "Activity 4", "Activity 5"
            )
        )
    )

    Scaffold(topBar = {
        SecondaryTopBar(title = stringResource(R.string.historico_de_alergias), onBackClick = {
            navController.popBackStack()
        })
    },
        floatingActionButton = {
            SBButtonPrimary(
                label = "Novo registro",
                onClick = {
                    showDialog = true
                },
                leftIcon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Novo registro"
                    )
                },
                modifier = Modifier.requiredWidth(240.dp)
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Timeline(
                timelineEventList = allergyHistory, modifier = Modifier
                    .padding(
                        horizontal = 8.dp,
                        vertical = 0.dp
                    )
            )


        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            SBButtonPrimary(
                label = "Novo registro",
                onClick = {
                    navController.navigate("add_new_record")
                },
                leftIcon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Novo registro"
                    )
                }
            )
        }

        if (showDialog) {
            AddNewRecordModal(
                navController = navController,
                allergyHistoryViewModel = AllergyHistoryViewModel(),
                onDismiss = { showDialog = false },
                onSaveRecord = {
                    showDialog = false
                }
            )
        }

    }
}