package com.phasmofriend.app.ui.behavior

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.phasmofriend.app.R
import com.phasmofriend.app.model.EvidenceState
import com.phasmofriend.app.ui.components.BehaviorRow
import com.phasmofriend.app.ui.components.PhasmoCard
import com.phasmofriend.app.ui.shared.InvestigationViewModel
import java.util.Locale

@Composable
fun BehaviorScreen(viewModel: InvestigationViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.title_behavior), style = MaterialTheme.typography.headlineSmall)
        Text(
            text = stringResource(R.string.behavior_subtitle_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.behavior_search_hint)) },
            singleLine = true
        )

        val sortedBehaviors = remember(uiState.behaviors) {
            uiState.behaviors.sortedBy { it.label.lowercase(Locale.getDefault()) }
        }
        val filtered = remember(sortedBehaviors, searchText) {
            val query = searchText.trim().lowercase(Locale.getDefault())
            if (query.isEmpty()) {
                sortedBehaviors
            } else {
                sortedBehaviors.filter { it.label.lowercase(Locale.getDefault()).contains(query) }
            }
        }

        PhasmoCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .weight(1f)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filtered, key = { it.id }) { behavior ->
                    BehaviorRow(
                        label = behavior.label,
                        state = uiState.behaviorStates[behavior.id] ?: EvidenceState.OFF,
                        onClick = { viewModel.cycleBehavior(behavior.id) }
                    )
                }
            }
        }
    }
}
