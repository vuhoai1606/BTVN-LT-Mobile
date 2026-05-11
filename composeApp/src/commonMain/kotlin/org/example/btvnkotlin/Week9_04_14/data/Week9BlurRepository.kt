package org.example.btvnkotlin.Week9_04_14.data

import kotlinx.coroutines.flow.StateFlow
import org.example.btvnkotlin.Week9_04_14.ui.BlurUiState

interface Week9BlurRepository {
    val blurUiState: StateFlow<BlurUiState>
    fun applyBlur(blurLevel: Int)
    fun cancelWork()
}

