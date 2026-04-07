package org.example.btvnkotlin.Thi_GK.data.cloudinary

import android.util.Log
import org.example.btvnkotlin.Thi_GK.util.ImagePicker

/**
 * Android implementation - Sử dụng Cloudinary Android SDK
 */
actual object CloudinaryUploader {
    private const val TAG = "CloudinaryUploader"
    
    actual suspend fun uploadLastPickedImage(): String {
        val uri = ImagePicker.getLastPickedUri()
        
        if (uri == null) {
            Log.e(TAG, "❌ No image selected (uri is null)")
            return ""
        }
        
        Log.d(TAG, "🔵 Starting upload with Uri: $uri")
        
        return try {
            val result = CloudinaryAndroidRepository.uploadImage(uri)
            if (result.isSuccess) {
                val url = result.getOrNull() ?: ""
                Log.d(TAG, "✅ Upload SUCCESS: $url")
                url
            } else {
                Log.e(TAG, "❌ Upload FAILED: ${result.exceptionOrNull()?.message}")
                ""
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Upload exception: ${e.message}", e)
            ""
        }
    }
}
