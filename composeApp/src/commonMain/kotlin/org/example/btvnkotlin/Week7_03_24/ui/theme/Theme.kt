package org.example.btvnkotlin.Week7_03_24.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = BackgroundWhite,
    background = BackgroundWhite,
    onBackground = TextBlack
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        content = content
    )
}