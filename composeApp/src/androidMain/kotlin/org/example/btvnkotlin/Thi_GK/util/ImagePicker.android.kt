package org.example.btvnkotlin.Thi_GK.util

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import java.io.InputStream

/**
 * Callback khi chọn ảnh (dùng Uri cho Android)
 */
typealias OnImagePickedCallback = (ByteArray?, Any?) -> Unit

/**
 * Android implementation của ImagePicker
 * Trả về cả ByteArray và Uri để có thể dùng với Cloudinary SDK
 */
actual object ImagePicker {
    // Lưu Uri gần nhất được chọn (dùng cho Android SDK upload)
    private var lastPickedUri: Uri? = null
    
    fun getLastPickedUri(): Uri? = lastPickedUri
    
    @Composable
    actual fun PickImage(onImagePicked: (ByteArray?) -> Unit) {
        val context = LocalContext.current
        
        println("📷 ImagePicker: Composable created")
        
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            println("📷 ImagePicker: Result received, uri = $uri")
            
            if (uri != null) {
                try {
                    // Lưu Uri để dùng với Cloudinary Android SDK
                    lastPickedUri = uri
                    println("📷 ImagePicker: Saved uri for Cloudinary SDK")
                    
                    // Đọc bytes cho preview (nếu cần)
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes()
                    inputStream?.close()
                    
                    println("📷 ImagePicker: Read ${bytes?.size ?: 0} bytes")
                    println("✅ ImagePicker: SUCCESS! Uri saved and bytes available")
                    onImagePicked(bytes)
                } catch (e: Exception) {
                    println("❌ ImagePicker: Exception: ${e.message}")
                    e.printStackTrace()
                    onImagePicked(null)
                }
            } else {
                println("⚠️ ImagePicker: User cancelled (uri is null)")
                lastPickedUri = null
                onImagePicked(null)
            }
        }
        
        LaunchedEffect(Unit) {
            println("📷 ImagePicker: Launching picker...")
            launcher.launch("image/*")
        }
    }
}
