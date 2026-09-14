package com.prep.transpetro.ui.screens.errorlog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prep.transpetro.domain.model.StudyConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorLogDetailScreen(
    entryId: Long,
    onBack: () -> Unit,
    vm: ErrorLogDetailViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(entryId) { vm.load(entryId) }
    LaunchedEffect(state.isSaved) { if (state.isSaved) onBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNew) "Novo erro" else "Editar erro") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Bloco", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                StudyConstants.BLOCKS.take(6).forEach { block ->
                    FilterChip(
                        selected = state.blockCode == block.code,
                        onClick = { vm.setBlockCode(block.code) },
                        label = { Text(block.code, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                StudyConstants.BLOCKS.drop(6).take(6).forEach { block ->
                    FilterChip(
                        selected = state.blockCode == block.code,
                        onClick = { vm.setBlockCode(block.code) },
                        label = { Text(block.code, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                StudyConstants.BLOCKS.drop(12).forEach { block ->
                    FilterChip(
                        selected = state.blockCode == block.code,
                        onClick = { vm.setBlockCode(block.code) },
                        label = { Text(block.code, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            Text("Motivo do erro", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                StudyConstants.ERROR_REASON_LABELS.entries.forEach { (code, label) ->
                    FilterChip(
                        selected = state.errorReason == code,
                        onClick = { vm.setErrorReason(code) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            OutlinedTextField(
                value = state.topicNorm,
                onValueChange = vm::setTopicNorm,
                label = { Text("Topico / assunto") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.triggerWord,
                onValueChange = vm::setTriggerWord,
                label = { Text("Palavra-gatilho (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.myAnswer,
                onValueChange = vm::setMyAnswer,
                label = { Text("Minha resposta") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            OutlinedTextField(
                value = state.correctAnswer,
                onValueChange = vm::setCorrectAnswer,
                label = { Text("Resposta correta") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            OutlinedTextField(
                value = state.correctionLine,
                onValueChange = vm::setCorrectionLine,
                label = { Text("Linha de correcao (regra, formula, conceito-chave)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(onClick = vm::save, modifier = Modifier.fillMaxWidth()) {
                Text("Salvar")
            }
        }
    }
}
