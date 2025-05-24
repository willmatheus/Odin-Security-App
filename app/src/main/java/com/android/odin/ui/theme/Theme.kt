package com.android.odin.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = YellowPrimary,
    onPrimary = Color.Black,
    background = Gray900,
    surface = Gray800,
    onSurface = Color.White,
    onBackground = Color.White,
    secondary = YellowLight,
    error = ErrorRed
)

@Composable
fun OdinTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}