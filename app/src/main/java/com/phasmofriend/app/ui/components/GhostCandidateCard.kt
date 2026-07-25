package com.phasmofriend.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.phasmofriend.app.deduction.DeductionResult
import com.phasmofriend.app.model.DangerLevel
import com.phasmofriend.app.model.Evidence

@Composable
fun GhostCandidateCard(
    result: DeductionResult,
    dangerLevel: DangerLevel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ghost = result.ghost
    val dangerColor = colorResource(dangerLevel.colorResId)

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (result.eliminated) 0.55f else 1f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, dangerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(ghost.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)

            Text(
                text = buildDescriptionText(ghost.description, result),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            EvidenceIconRow(ghost.evidences, ghost.extraEvidence)

            Text(
                text = buildEvidenceText(ghost.evidences, ghost.extraEvidence),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            DangerChip(dangerLevel)
        }
    }
}

@Composable
private fun EvidenceIconRow(evidences: Set<Evidence>, extraEvidence: Set<Evidence>) {
    val all = evidences + extraEvidence
    if (all.isEmpty()) return

    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        all.forEach { evidence ->
            Image(
                painter = painterResource(evidence.iconResId),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .alpha(if (evidence in extraEvidence) 0.65f else 1f)
            )
        }
    }
}

private fun buildDescriptionText(baseDescription: String, result: DeductionResult): String {
    val lines = mutableListOf(baseDescription)

    lines += when {
        result.eliminated -> "Status: Eliminated by hard evidence conflict."
        result.matchPercent > 0 -> "Match: ${result.matchPercent}%"
        else -> "Match: Add evidence or behavior clues to start narrowing ghosts."
    }

    result.matchedReasons.take(2).forEach { lines += "Why: $it" }
    result.warnings.take(1).forEach { lines += "Watch: $it" }
    result.contradictions.take(2).forEach { lines += "Against: $it" }

    return lines.joinToString("\n")
}

private fun buildEvidenceText(evidences: Set<Evidence>, extraEvidence: Set<Evidence>): String {
    val normal = evidences.joinToString(" • ") { it.shortName }

    return if (extraEvidence.isEmpty()) {
        "Evidence: $normal"
    } else {
        "Evidence: $normal\nExtra: ${extraEvidence.joinToString(" • ") { it.shortName }}"
    }
}
