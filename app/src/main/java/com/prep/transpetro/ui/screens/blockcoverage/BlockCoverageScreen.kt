package com.prep.transpetro.ui.screens.blockcoverage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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
import com.prep.transpetro.ui.theme.Amber500
import com.prep.transpetro.ui.theme.GreenSuccess
import com.prep.transpetro.ui.theme.RedDanger

private val CONFIDENCE_LABELS = listOf("—", "Fraco", "Ok", "Solido")

@Composable
fun BlockCoverageScreen(
    vm: BlockCoverageViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    Scaffold { padding ->
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                Text(
                    "COBERTURA DE BLOCOS",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(state.items, key = { it.code }) { item ->
                val confidenceColor = when (item.confidence) {
                    0 -> MaterialTheme.colorScheme.onSurfaceVariant
                    1 -> RedDanger
                    2 -> Amber500
                    else -> GreenSuccess
                }
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        item.code,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        item.subject,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(item.name, style = MaterialTheme.typography.bodyMedium)
                                if (item.lastStudied != null) {
                                    Text(
                                        "Ultima vez: ${item.lastStudied}  •  ${item.timesStudied}x",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                CONFIDENCE_LABELS.getOrElse(item.confidence) { "—" },
                                style = MaterialTheme.typography.labelMedium,
                                color = confidenceColor
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            CONFIDENCE_LABELS.indices.forEach { level ->
                                FilterChip(
                                    selected = item.confidence == level,
                                    onClick = { vm.setConfidence(item.code, level) },
                                    label = {
                                        Text(
                                            CONFIDENCE_LABELS[level],
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
