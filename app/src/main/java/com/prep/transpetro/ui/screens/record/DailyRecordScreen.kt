package com.prep.transpetro.ui.screens.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prep.transpetro.domain.model.StudyConstants
import com.prep.transpetro.ui.theme.GreenSuccess

@Composable
fun DailyRecordScreen(vm: DailyRecordViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()

    if (state.isSaved) {
        LaunchedEffect(Unit) { /* toast handled by snackbar host if wired */ }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("REGISTRO DIARIO", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(state.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

        if (state.todayPlanDescription.isNotEmpty()) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Text(
                    text = state.todayPlanDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Status
        Text("Status do estudo", style = MaterialTheme.typography.headlineSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("SIM" to "Estudei", "PARCIAL" to "Parcial", "NAO" to "Nao").forEach { (code, label) ->
                FilterChip(
                    selected = state.status == code,
                    onClick = { vm.setStatus(code) },
                    label = { Text(label) }
                )
            }
        }

        // Block
        Text("Bloco estudado", style = MaterialTheme.typography.headlineSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            StudyConstants.BLOCK_CODES_SELECTABLE.chunked(5).forEach { chunk ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    chunk.forEach { code ->
                        FilterChip(
                            selected = state.blockCode == code,
                            onClick = { vm.setBlockCode(code) },
                            label = { Text(code, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }
        }

        // Hours
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Horas estudadas", style = MaterialTheme.typography.headlineSmall)
            Text(
                "${"%.1f".format(state.hoursStudied)}h",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = state.hoursStudied.toFloat(),
            onValueChange = { vm.setHours(it.toDouble()) },
            valueRange = 0f..6f,
            steps = 11
        )

        // Questions
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = if (state.questionsDone == 0) "" else state.questionsDone.toString(),
                onValueChange = { vm.setQuestions(it.toIntOrNull() ?: 0) },
                label = { Text("Questoes feitas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = if (state.correctAnswers == 0) "" else state.correctAnswers.toString(),
                onValueChange = { vm.setCorrect(it.toIntOrNull() ?: 0) },
                label = { Text("Acertos") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }
        if (state.questionsDone > 0) {
            Text(
                "Aproveitamento: ${"%.0f".format(state.accuracy)}%",
                style = MaterialTheme.typography.bodyMedium,
                color = if (state.accuracy >= 70f) GreenSuccess else MaterialTheme.colorScheme.tertiary
            )
        }

        // Energy
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Energia (1-5)", style = MaterialTheme.typography.headlineSmall)
            Text("${state.energyLevel}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
        }
        Slider(
            value = state.energyLevel.toFloat(),
            onValueChange = { vm.setEnergy(it.toInt()) },
            valueRange = 1f..5f,
            steps = 3
        )

        // Notes
        OutlinedTextField(
            value = state.notes,
            onValueChange = vm::setNotes,
            label = { Text("Observacoes") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(Modifier.height(8.dp))
        Button(
            onClick = vm::save,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.isSaved) "Salvo" else "Salvar registro")
        }
    }
}
