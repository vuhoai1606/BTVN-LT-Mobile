package org.example.btvnkotlin.Week9_04_14.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.btvnkotlin.Week9_04_14.ui.BlurUiState

class UnsupportedBlurRepository(
    private val platformName: String
) : Week9BlurRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _blurUiState = MutableStateFlow<BlurUiState>(BlurUiState.Idle)
    override val blurUiState: StateFlow<BlurUiState> = _blurUiState.asStateFlow()

    override fun applyBlur(blurLevel: Int) {
        scope.launch {
            _blurUiState.value = BlurUiState.Loading
            delay(400)
            _blurUiState.value = BlurUiState.Error(
                "WorkManager demo chỉ chạy trên Android. Platform hiện tại: $platformName"
            )
        }
    }

    override fun cancelWork() {
        _blurUiState.value = BlurUiState.Idle
    }
}

