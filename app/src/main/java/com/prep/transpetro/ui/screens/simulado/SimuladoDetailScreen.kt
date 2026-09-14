package com.prep.transpetro.ui.screens.simulado

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prep.transpetro.domain.model.CutoffStatus
import com.prep.transpetro.domain.model.StudyConstants
import com.prep.transpetro.ui.theme.Amber500
import com.prep.transpetro.ui.theme.GreenSuccess
import com.prep.transpetro.ui.theme.RedDanger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimuladoDetailScreen(
    simuladoId: Long,
    onBack: () -> Unit,
    vm: SimuladoDetailViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(simuladoId) { vm.load(simuladoId) }
    LaunchedEffect(state.isSaved) { if (state.isSaved) onBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) "Novo simulado" else "Simulado ${state.date}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.date,
                onValueChange = vm::setDate,
                label = { Text("Data (AAAA-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("DIAGNOSTICO", "PARCIAL", "COMPLETO", "VELOCIDADE").forEach { t ->
                    FilterChip(
                        selected = state.type == t,
                        onClick = { vm.setType(t) },
                        label = { Text(t.take(4), style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Semana", style = MaterialTheme.typography.headlineSmall)
                Text("${state.weekNumber}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            }
            Slider(
                value = state.weekNumber.toFloat(),
                onValueChange = { vm.setWeek(it.toInt()) },
                valueRange = 1f..12f,
                steps = 10
            )

            // Score sliders
            ScoreSlider("Seguranca", state.securityCorrect, StudyConstants.SECURITY_QUESTIONS, vm::setSecurity)
            ScoreSlider("Portugues", state.portugueseCorrect, StudyConstants.PORTUGUESE_QUESTIONS, vm::setPortuguese)
            ScoreSlider("Matematica", state.mathCorrect, StudyConstants.MATH_QUESTIONS, vm::setMath)

            // Cutoff status
            val (label, color) = when (val s = state.cutoffStatus) {
                is CutoffStatus.PassesAll -> "Aprovado em todas as notas de corte" to GreenSuccess
                is CutoffStatus.EliminatedZero -> "ELIMINADO — zero em ${s.subject}" to RedDanger
                is CutoffStatus.BelowSecurityMinimum -> "Abaixo do minimo em Seguranca (${StudyConstants.MIN_SECURITY_PASS})" to Amber500
                is CutoffStatus.BelowGeneralMinimum -> "Abaixo do minimo geral (${StudyConstants.MIN_GENERAL_PASS})" to Amber500
            }
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = color,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(Modifier.height(8.dp))
            Button(onClick = vm::save, modifier = Modifier.fillMaxWidth()) {
                Text("Salvar")
            }
        }
    }
}

@Composable
private fun ScoreSlider(label: String, value: Int, max: Int, onChanged: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.headlineSmall)
        Text("$value/$max", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
    }
    Slider(
        value = value.toFloat(),
        onValueChange = { onChanged(it.toInt()) },
        valueRange = 0f..max.toFloat(),
        steps = max - 1
    )
}
