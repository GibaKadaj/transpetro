package com.prep.transpetro.ui.screens.studyplan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun StudyPlanScreen(
    vm: StudyPlanViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val nextSevenDays = (0L..6L).map {
        LocalDate.now().plusDays(it).format(DateTimeFormatter.ISO_DATE)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.startNew(LocalDate.now().toString()) }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar dia")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                "PLANO DE ESTUDO",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(
                    (nextSevenDays + state.plans.map { it.date }).distinct().sorted(),
                    key = { it }
                ) { date ->
                    val plan = state.plans.firstOrNull { it.date == date }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (plan != null)
                                MaterialTheme.colorScheme.surface
                            else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(date, style = MaterialTheme.typography.bodyMedium)
                                if (plan != null) {
                                    Text(
                                        "${plan.plannedHours}h — ${plan.plannedBlocks.ifBlank { "sem blocos" }}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (plan.notes.isNotBlank()) {
                                        Text(
                                            plan.notes,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                } else {
                                    Text(
                                        "Sem plano",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            IconButton(onClick = {
                                if (plan != null) vm.startEdit(plan) else vm.startNew(date)
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                        }
                    }
                }
            }
        }

        if (state.editingDate != null) {
            AlertDialog(
                onDismissRequest = vm::cancelEdit,
                title = { Text("Plano para ${state.editingDate}") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Horas planejadas")
                            Text(
                                "%.1f h".format(state.editHours),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Slider(
                            value = state.editHours,
                            onValueChange = vm::setHours,
                            valueRange = 0.5f..4f,
                            steps = 6
                        )
                        OutlinedTextField(
                            value = state.editBlocks,
                            onValueChange = vm::setBlocks,
                            label = { Text("Blocos (ex: B01, B03)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = state.editNotes,
                            onValueChange = vm::setNotes,
                            label = { Text("Observacoes") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                    }
                },
                confirmButton = { TextButton(onClick = vm::save) { Text("Salvar") } },
                dismissButton = { TextButton(onClick = vm::cancelEdit) { Text("Cancelar") } }
            )
        }
    }
}
