package org.example.btvnkotlin.Week9_04_14.data

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.example.btvnkotlin.Week9_04_14.ui.BlurUiState
import org.example.btvnkotlin.Week9_04_14.workers.BlurWorker
import org.example.btvnkotlin.Week9_04_14.workers.CleanupWorker
import org.example.btvnkotlin.Week9_04_14.workers.SaveImageToFileWorker
import org.example.btvnkotlin.Week9_04_14.workers.getStartingImageUri

class AndroidWorkManagerBlurRepository(
    private val context: Context
) : Week9BlurRepository {
    private val workerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val workManager = WorkManager.getInstance(context)
    private val imageUri = getStartingImageUri(context)

    override val blurUiState: StateFlow<BlurUiState> = workManager
        .getWorkInfosForUniqueWorkFlow(IMAGE_MANIPULATION_WORK_NAME)
        .map(::mapWorkInfoToState)
        .stateIn(
            scope = workerScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BlurUiState.Idle
        )

    override fun applyBlur(blurLevel: Int) {
        val cleanupRequest = OneTimeWorkRequestBuilder<CleanupWorker>().build()
        var continuation = workManager.beginUniqueWork(
            IMAGE_MANIPULATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            cleanupRequest
        )

        repeat(blurLevel.coerceIn(1, 3)) { index ->
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()
            if (index == 0) {
                blurBuilder.setInputData(workDataOf(KEY_IMAGE_URI to imageUri.toString()))
            }
            continuation = continuation.then(blurBuilder.build())
        }

        val saveRequest = OneTimeWorkRequestBuilder<SaveImageToFileWorker>().build()
        continuation.then(saveRequest).enqueue()
    }

    override fun cancelWork() {
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    private fun mapWorkInfoToState(workInfos: List<WorkInfo>): BlurUiState {
        if (workInfos.isEmpty()) {
            return BlurUiState.Idle
        }

        if (workInfos.any { it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.BLOCKED }) {
            return BlurUiState.Loading
        }

        if (workInfos.any { it.state == WorkInfo.State.FAILED }) {
            return BlurUiState.Error("Không thể blur ảnh.")
        }

        if (workInfos.any { it.state == WorkInfo.State.CANCELLED }) {
            return BlurUiState.Error("Đã hủy tác vụ blur.")
        }

        val outputUri = workInfos
            .lastOrNull()
            ?.outputData
            ?.getString(KEY_IMAGE_URI)
            .orEmpty()

        return if (outputUri.isNotEmpty()) {
            BlurUiState.Completed(outputUri)
        } else {
            BlurUiState.Idle
        }
    }
}

