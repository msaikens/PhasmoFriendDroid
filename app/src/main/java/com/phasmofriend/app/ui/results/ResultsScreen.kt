package com.phasmofriend.app.ui.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.phasmofriend.app.R
import com.phasmofriend.app.ui.components.GhostCandidateCard
import com.phasmofriend.app.ui.components.NextBestTestCard
import com.phasmofriend.app.ui.shared.InvestigationViewModel

@Composable
fun ResultsScreen(
    viewModel: InvestigationViewModel,
    onGhostClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.results_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        uiState.nextBestTest?.let { nextBestTest ->
            NextBestTestCard(
                nextBestTest = nextBestTest,
                onPassed = { viewModel.markTestPassed(nextBestTest.subject) },
                onFailed = { viewModel.markTestFailed(nextBestTest.subject) },
                onInconclusive = { viewModel.markTestInconclusive(nextBestTest.subject) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(uiState.candidates, key = { it.ghost.id }) { result ->
                GhostCandidateCard(
                    result = result,
                    dangerLevel = uiState.dangerLevelFor(result.ghost),
                    onClick = { onGhostClick(result.ghost.id) },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                )
            }
        }
    }
}
