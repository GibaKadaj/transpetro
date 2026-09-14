package com.prep.transpetro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PetroBlue,
    onPrimary = Charcoal050,
    primaryContainer = PetroBlueDim,
    onPrimaryContainer = Charcoal100,
    secondary = Charcoal500,
    onSecondary = Charcoal050,
    secondaryContainer = Charcoal700,
    onSecondaryContainer = Charcoal100,
    tertiary = Amber500,
    onTertiary = Charcoal950,
    tertiaryContainer = Color(0xFF3D2E00),
    onTertiaryContainer = Amber300,
    background = Charcoal950,
    onBackground = Charcoal100,
    surface = Charcoal900,
    onSurface = Charcoal100,
    surfaceVariant = Charcoal800,
    onSurfaceVariant = Charcoal300,
    outline = Charcoal600,
    outlineVariant = Charcoal700,
    error = RedDanger,
    onError = Charcoal050,
    errorContainer = Color(0xFF4A1515),
    onErrorContainer = Color(0xFFFFB3B3),
    inverseSurface = Charcoal100,
    inverseOnSurface = Charcoal900,
    inversePrimary = PetroBlueDim
)

@Composable
fun TranspetroTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = TranspetroTypography,
        content = content
    )
}
