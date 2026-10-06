package com.example.smartdigitalbuspass.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = RoyalBlue,
    onPrimary = SurfaceWhite,
    primaryContainer = RoyalBlueDark,
    onPrimaryContainer = SurfaceWhite,
    secondary = AccentGold,
    background = BackgroundLight,
    surface = SurfaceWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundLight,
    onSurfaceVariant = TextSecondary
)

@Composable
fun SmartDigitalBusPassTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
