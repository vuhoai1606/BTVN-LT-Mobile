package org.example.btvnkotlin.Thi_GK.util

import androidx.compose.runtime.Composable

/**
 * ImagePicker - Multiplatform image picker
 * Platform-specific implementations trong androidMain, iosMain, jvmMain
 */
expect object ImagePicker {
    /**
     * Launch image picker và trả về ByteArray của ảnh đã chọn
     * @param onImagePicked Callback với ByteArray của ảnh hoặc null nếu cancel
     */
    @Composable
    fun PickImage(onImagePicked: (ByteArray?) -> Unit)
}

/**
 * Data class để wrap kết quả pick ảnh
 */
data class PickedImage(
    val bytes: ByteArray,
    val fileName: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PickedImage

        if (!bytes.contentEquals(other.bytes)) return false
        if (fileName != other.fileName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + fileName.hashCode()
        return result
    }
}
