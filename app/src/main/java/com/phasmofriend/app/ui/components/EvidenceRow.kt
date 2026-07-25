package com.phasmofriend.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.phasmofriend.app.model.EvidenceState

/**
 * Cycles OFF -> HAS -> NOT -> OFF on tap. The border color itself carries the
 * current state (not just the small leading icon), so the tappable/cyclable
 * nature of the row is obvious even before the user has tried it.
 */
@Composable
fun EvidenceRow(
    icon: Painter,
    label: String,
    state: EvidenceState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRuledOut = state == EvidenceState.NOT
    val rowAlpha = if (isRuledOut) 0.6f else 1f

    val borderColor = when (state) {
        EvidenceState.OFF -> MaterialTheme.colorScheme.outline
        EvidenceState.HAS -> MaterialTheme.colorScheme.secondary
        EvidenceState.NOT -> MaterialTheme.colorScheme.error
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(if (state == EvidenceState.OFF) 1.dp else 2.dp, borderColor),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(28.dp)
                    .alpha(rowAlpha)
            )
            Spacer(Modifier.width(16.dp))
            StateIndicator(state)
            Spacer(Modifier.width(12.dp))
            Text(
                text = label,
                modifier = Modifier
                    .weight(1f)
                    .alpha(rowAlpha),
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (isRuledOut) TextDecoration.LineThrough else TextDecoration.None
            )
        }
    }
}

@Composable
private fun StateIndicator(state: EvidenceState) {
    when (state) {
        EvidenceState.OFF -> Spacer(Modifier.size(22.dp))

        EvidenceState.HAS -> Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Confirmed",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(22.dp)
        )

        EvidenceState.NOT -> Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Ruled out",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(22.dp)
        )
    }
}
