package com.renatojobal.kmptemplate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Accent,
    onPrimary = Slate50,
    primaryContainer = AccentLight,
    onPrimaryContainer = AccentDark,
    secondary = Slate700,
    onSecondary = Slate50,
    secondaryContainer = Slate200,
    onSecondaryContainer = Slate800,
    background = Slate50,
    onBackground = Slate900,
    surface = Slate50,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    outline = Slate300,
    error = Danger,
    onError = Slate50,
    errorContainer = DangerLight,
    onErrorContainer = Stone900,
)

private val DarkColorScheme = darkColorScheme(
    primary = AccentLight,
    onPrimary = AccentDark,
    primaryContainer = AccentDark,
    onPrimaryContainer = AccentLight,
    secondary = Slate300,
    onSecondary = Slate900,
    secondaryContainer = Slate700,
    onSecondaryContainer = Slate100,
    background = Stone900,
    onBackground = Slate100,
    surface = Stone800,
    onSurface = Slate100,
    surfaceVariant = Stone700,
    onSurfaceVariant = Slate300,
    outline = Slate500,
    error = DangerLight,
    onError = Stone900,
    errorContainer = Danger,
    onErrorContainer = Slate50,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = AppTypography,
        content = content,
    )
}
