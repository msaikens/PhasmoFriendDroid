package com.phasmofriend.app.ui.evidence

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.phasmofriend.app.R
import com.phasmofriend.app.model.Evidence
import com.phasmofriend.app.model.EvidenceMode
import com.phasmofriend.app.model.EvidenceState
import com.phasmofriend.app.ui.components.EvidenceRow
import com.phasmofriend.app.ui.components.PhasmoCard
import com.phasmofriend.app.ui.shared.InvestigationViewModel
import androidx.compose.foundation.layout.Row

@Composable
fun EvidenceScreen(viewModel: InvestigationViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.title_evidence), style = MaterialTheme.typography.headlineSmall)
        Text(
            text = stringResource(R.string.evidence_subtitle_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        PhasmoCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Investigation mode",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            EvidenceMode.entries.forEach { mode ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = uiState.evidenceMode == mode,
                            onClick = { viewModel.setEvidenceMode(mode) }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = uiState.evidenceMode == mode, onClick = null)
                    Text(mode.displayName, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        val sortedEvidence = Evidence.entries
            .map { evidence -> evidence to stringResource(evidence.labelResId) }
            .sortedBy { it.second }

        PhasmoCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sortedEvidence, key = { it.first.name }) { (evidence, label) ->
                    EvidenceRow(
                        icon = painterResource(evidence.iconResId),
                        label = label,
                        state = uiState.evidenceStates[evidence] ?: EvidenceState.OFF,
                        onClick = { viewModel.cycleEvidence(evidence) }
                    )
                }
            }
        }
    }
}
