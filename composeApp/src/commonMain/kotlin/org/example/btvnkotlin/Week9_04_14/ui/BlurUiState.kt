package org.example.btvnkotlin.Week9_04_14.ui

sealed interface BlurUiState {
    data object Idle : BlurUiState
    data object Loading : BlurUiState
    data class Completed(val outputUri: String) : BlurUiState
    data class Error(val message: String) : BlurUiState
}

