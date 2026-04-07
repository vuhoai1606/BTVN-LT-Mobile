package org.example.btvnkotlin.Thi_GK.data.cloudinary

import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * CloudinaryAndroidRepository - Upload ảnh sử dụng Cloudinary Android SDK
 * Copy từ MyNoteApp - cách này ổn định hơn dùng Ktor
 */
object CloudinaryAndroidRepository {
    private const val TAG = "CloudinaryRepo"
    
    /**
     * Upload ảnh từ Uri lên Cloudinary
     */
    suspend fun uploadImage(uri: Uri): Result<String> = suspendCancellableCoroutine { cont ->
        try {
            Log.d(TAG, "🔵 Starting upload: $uri")
            
            val requestId = MediaManager.get().upload(uri)
                .unsigned("ml_default")
                .option("folder", "notes")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) {
                        Log.d(TAG, "🔵 Upload started: $requestId")
                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                        val progress = (bytes * 100 / totalBytes).toInt()
                        Log.d(TAG, "🔵 Upload progress: $progress%")
                    }

                    override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                        val url = resultData?.get("secure_url") as? String
                        if (url != null) {
                            Log.d(TAG, "✅ Upload SUCCESS: $url")
                            cont.resume(Result.success(url))
                        } else {
                            Log.e(TAG, "❌ URL không hợp lệ")
                            cont.resume(Result.failure(Exception("URL không hợp lệ")))
                        }
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        Log.e(TAG, "❌ Upload error: ${error?.description}")
                        cont.resume(Result.failure(Exception(error?.description ?: "Upload thất bại")))
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                        Log.w(TAG, "⚠️ Upload rescheduled: ${error?.description}")
                    }
                })
                .dispatch()

            cont.invokeOnCancellation {
                MediaManager.get().cancelRequest(requestId)
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Upload exception: ${e.message}", e)
            cont.resume(Result.failure(e))
        }
    }
    
    /**
     * Tạo thumbnail URL từ original URL
     */
    fun getThumbnailUrl(originalUrl: String, width: Int = 200): String {
        return originalUrl.replace("/upload/", "/upload/w_$width,c_scale/")
    }
}
