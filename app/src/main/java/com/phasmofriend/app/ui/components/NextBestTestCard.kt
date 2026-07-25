package com.phasmofriend.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.phasmofriend.app.deduction.NextBestTest
import com.phasmofriend.app.deduction.TestSubject

@Composable
fun NextBestTestCard(
    nextBestTest: NextBestTest,
    onPassed: () -> Unit,
    onFailed: () -> Unit,
    onInconclusive: () -> Unit,
    modifier: Modifier = Modifier
) {
    PhasmoCard(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Next best test",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = nextBestTest.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = nextBestTest.detail,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = nextBestTest.meta,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (nextBestTest.subject != TestSubject.None) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPassed,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) { Text("Passed") }
                Button(
                    onClick = onFailed,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Failed") }
                OutlinedButton(onClick = onInconclusive) { Text("Inconclusive") }
            }
        }
    }
}
