package com.phasmofriend.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.phasmofriend.app.model.DangerLevel

@Composable
fun DangerChip(level: DangerLevel, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(level.labelResId),
        color = Color.White,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
            .background(colorResource(level.colorResId), RoundedCornerShape(percent = 50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
