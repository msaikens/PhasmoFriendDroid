package com.phasmofriend.app.ui.ghostdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.phasmofriend.app.R
import com.phasmofriend.app.model.DangerLevel
import com.phasmofriend.app.ui.shared.InvestigationViewModel

@Composable
fun GhostDetailScreen(
    ghostId: String,
    viewModel: InvestigationViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ghost = uiState.candidates.map { it.ghost }.firstOrNull { it.id == ghostId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_to_results))
            }
            Text(
                text = ghost?.name ?: stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall
            )
        }

        if (ghost == null) {
            Text(
                text = stringResource(R.string.ghost_detail_missing_data_fallback),
                modifier = Modifier.padding(top = 16.dp)
            )
            return@Column
        }

        val allEvidence = ghost.evidences + ghost.extraEvidence
        val allEvidenceLabels = allEvidence.map { stringResource(it.labelResId) }
        Text(
            text = stringResource(
                R.string.ghost_row_evidence_format,
                allEvidenceLabels.joinToString(" / ")
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = if (ghost.possibleBehaviors.isEmpty()) {
                stringResource(R.string.ghost_row_no_known_behavior)
            } else {
                stringResource(
                    R.string.ghost_row_behavior_format,
                    ghost.possibleBehaviors.joinToString(" • ") { it.label }
                )
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = ghost.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 12.dp, bottom = 16.dp)
        )

        HorizontalDivider()

        Text(
            text = stringResource(R.string.danger_level_label),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        val effectiveLevel = uiState.dangerLevelFor(ghost)

        Row(modifier = Modifier.fillMaxWidth()) {
            DangerLevel.entries.forEach { level ->
                Row(
                    modifier = Modifier.selectable(
                        selected = effectiveLevel == level,
                        onClick = { viewModel.setDangerLevel(ghost, level) }
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = effectiveLevel == level, onClick = null)
                    Text(stringResource(level.labelResId), modifier = Modifier.padding(end = 12.dp))
                }
            }
        }

        OutlinedButton(
            onClick = { viewModel.setDangerLevel(ghost, null) },
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        ) {
            Text(stringResource(R.string.danger_level_reset))
        }
    }
}
