package org.example.btvnkotlin.Week9_04_14.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import org.example.btvnkotlin.Week9_04_14.data.KEY_IMAGE_URI

class BlurWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val imageUri = inputData.getString(KEY_IMAGE_URI).orEmpty()
        if (imageUri.isBlank()) {
            return Result.failure()
        }

        val sourceBitmap = decodeBitmapFromUri(applicationContext, imageUri) ?: return Result.failure()
        val blurredBitmap = blurBitmap(sourceBitmap)
        val outputUri = writeBitmapToFile(
            context = applicationContext,
            bitmap = blurredBitmap,
            prefix = "tmp"
        )

        return Result.success(workDataOf(KEY_IMAGE_URI to outputUri.toString()))
    }
}

