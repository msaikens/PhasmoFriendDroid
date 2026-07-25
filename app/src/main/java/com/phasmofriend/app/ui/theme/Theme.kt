package com.phasmofriend.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dark-only for now, matching the app's existing always-dark visual identity.
private val PhasmoDarkColorScheme = darkColorScheme(
    primary = PhasmoPurple,
    onPrimary = Color.White,
    primaryContainer = PhasmoPurpleDark,
    onPrimaryContainer = Color.White,
    secondary = PhasmoTeal,
    onSecondary = Color.Black,
    secondaryContainer = PhasmoTealDark,
    onSecondaryContainer = Color.White,
    background = PhasmoBackground,
    onBackground = PhasmoTextPrimary,
    surface = PhasmoSurface,
    onSurface = PhasmoTextPrimary,
    surfaceVariant = PhasmoSurfaceHigh,
    onSurfaceVariant = PhasmoTextSecondary,
    outline = PhasmoOutline,
)

@Composable
fun PhasmoFriendTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PhasmoDarkColorScheme,
        typography = PhasmoTypography,
        content = content
    )
}
