package com.phasmofriend.app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.phasmofriend.app.model.EvidenceState

@Composable
fun EvidenceRow(
    icon: Painter,
    label: String,
    state: EvidenceState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ClueRow(
        label = label,
        state = state,
        onClick = onClick,
        modifier = modifier,
        leadingIcon = { alpha ->
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp).alpha(alpha)
            )
        }
    )
}
