package org.example.btvnkotlin.Week9_04_14.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberWeek9BlurRepository(): Week9BlurRepository {
    val context = LocalContext.current.applicationContext
    return remember(context) { AndroidWorkManagerBlurRepository(context) }
}

