package org.example.btvnkotlin.Week9_04_14.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.example.btvnkotlin.Week9_04_14.data.OUTPUT_PATH
import java.io.File

class CleanupWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
        if (!outputDirectory.exists()) {
            return Result.success()
        }

        outputDirectory.listFiles()
            ?.filter { it.name.startsWith("tmp-") && it.name.endsWith(".png") }
            ?.forEach { it.delete() }

        return Result.success()
    }
}

