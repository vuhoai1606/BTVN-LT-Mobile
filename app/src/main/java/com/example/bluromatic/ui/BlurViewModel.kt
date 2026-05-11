/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bluromatic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.WorkInfo
import com.example.bluromatic.BluromaticApplication
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.data.BlurAmountData
import com.example.bluromatic.data.BluromaticRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class BlurViewModel(private val bluromaticRepository: BluromaticRepository) : ViewModel() {

    internal val blurAmount = BlurAmountData.blurAmount

    // Biến này để theo dõi xem người dùng đã bấm Start trong phiên này chưa
    private val _isBlurringStarted = MutableStateFlow(false)

    val blurUiState: StateFlow<BlurUiState> = combine(
        bluromaticRepository.outputWorkInfo,
        _isBlurringStarted
    ) { info, started ->
        if (!started) {
            // Nếu chưa bấm Start trong phiên này, luôn hiện trạng thái mặc định
            BlurUiState.Default
        } else {
            when {
                info?.state?.isFinished == true -> {
                    val outputImageUri = info.outputData.getString(KEY_IMAGE_URI)
                    if (!outputImageUri.isNullOrEmpty()) {
                        BlurUiState.Complete(outputImageUri)
                    } else {
                        BlurUiState.Default
                    }
                }
                info?.state == WorkInfo.State.RUNNING || 
                info?.state == WorkInfo.State.ENQUEUED || 
                info?.state == WorkInfo.State.BLOCKED -> {
                    BlurUiState.Loading
                }
                else -> {
                    BlurUiState.Default
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BlurUiState.Default
    )

    fun applyBlur(blurLevel: Int) {
        _isBlurringStarted.value = true
        bluromaticRepository.applyBlur(blurLevel)
    }

    fun cancelWork() {
        _isBlurringStarted.value = false
        bluromaticRepository.cancelWork()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val bluromaticRepository =
                    (this[APPLICATION_KEY] as BluromaticApplication).container.bluromaticRepository
                BlurViewModel(bluromaticRepository = bluromaticRepository)
            }
        }
    }
}

sealed interface BlurUiState {
    object Default : BlurUiState
    object Loading : BlurUiState
    data class Complete(val outputUri: String) : BlurUiState
}
