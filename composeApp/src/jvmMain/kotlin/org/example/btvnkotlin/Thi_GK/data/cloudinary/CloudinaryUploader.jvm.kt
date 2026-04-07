package org.example.btvnkotlin.Thi_GK.data.cloudinary

/**
 * JVM/Desktop implementation - Fallback dùng HTTP
 * (Desktop không có SDK, dùng CloudinaryService cũ)
 */
actual object CloudinaryUploader {
    private var lastBytes: ByteArray? = null
    private var lastName: String = ""
    
    fun setLastPickedBytes(bytes: ByteArray?, name: String) {
        lastBytes = bytes
        lastName = name
    }
    
    actual suspend fun uploadLastPickedImage(): String {
        val bytes = lastBytes
        if (bytes == null || bytes.isEmpty()) {
            println("❌ JVM CloudinaryUploader: No image bytes")
            return ""
        }
        
        return try {
            val service = CloudinaryService("dwzsc2t3i", "ml_default")
            val result = service.uploadImage(bytes, lastName)
            service.close()
            
            when (result) {
                is CloudinaryUploadResult.Success -> result.url
                is CloudinaryUploadResult.Error -> {
                    println("❌ JVM upload error: ${result.message}")
                    ""
                }
            }
        } catch (e: Exception) {
            println("❌ JVM upload exception: ${e.message}")
            ""
        }
    }
}
