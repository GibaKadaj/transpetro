package com.prep.transpetro.ui.screens.simulado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prep.transpetro.domain.model.CutoffStatus
import com.prep.transpetro.ui.theme.Amber500
import com.prep.transpetro.ui.theme.GreenSuccess
import com.prep.transpetro.ui.theme.RedDanger

@Composable
fun SimuladoScreen(
    onNavigateToDetail: (Long) -> Unit,
    vm: SimuladoViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToDetail(-1L) }) {
                Icon(Icons.Default.Add, contentDescription = "Novo simulado")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                Text(
                    "SIMULADOS",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(state.simulados, key = { it.id }) { sim ->
                val cutoff = vm.cutoffFor(sim)
                val cutoffColor = when (cutoff) {
                    is CutoffStatus.PassesAll -> GreenSuccess
                    is CutoffStatus.EliminatedZero -> RedDanger
                    else -> Amber500
                }
                val cutoffLabel = when (cutoff) {
                    is CutoffStatus.PassesAll -> "Aprovado"
                    is CutoffStatus.EliminatedZero -> "Eliminado"
                    is CutoffStatus.BelowSecurityMinimum -> "Abaixo min. SEG"
                    is CutoffStatus.BelowGeneralMinimum -> "Abaixo min. geral"
                }
                Card(
                    onClick = { onNavigateToDetail(sim.id) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (sim.isPlanned)
                            MaterialTheme.colorScheme.surfaceVariant
                        else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(sim.date, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "S${sim.weekNumber}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (sim.isPlanned) {
                                    Text(
                                        "PLANEJADO",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("SEG ${sim.securityCorrect}/40", style = MaterialTheme.typography.bodySmall)
                                Text("PT ${sim.portugueseCorrect}/10", style = MaterialTheme.typography.bodySmall)
                                Text("MT ${sim.mathCorrect}/10", style = MaterialTheme.typography.bodySmall)
                            }
                            if (!sim.isPlanned) {
                                Text(
                                    cutoffLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = cutoffColor
                                )
                            }
                        }
                        if (!sim.isPlanned) {
                            IconButton(onClick = { vm.delete(sim) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}
