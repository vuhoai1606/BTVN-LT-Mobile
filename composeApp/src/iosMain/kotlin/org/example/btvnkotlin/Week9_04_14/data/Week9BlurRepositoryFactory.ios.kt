package org.example.btvnkotlin.Week9_04_14.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.example.btvnkotlin.getPlatform

@Composable
actual fun rememberWeek9BlurRepository(): Week9BlurRepository {
    return remember { UnsupportedBlurRepository(getPlatform().name) }
}

