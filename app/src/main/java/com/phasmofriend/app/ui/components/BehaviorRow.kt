package com.phasmofriend.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.phasmofriend.app.model.EvidenceState

@Composable
fun BehaviorRow(
    label: String,
    state: EvidenceState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ClueRow(
        label = label,
        state = state,
        onClick = onClick,
        modifier = modifier
    )
}
