package org.example.btvnkotlin.Thi_GK.util

import androidx.compose.runtime.Composable

/**
 * iOS implementation của ImagePicker
 * TODO: Implement with UIImagePickerController
 */
actual object ImagePicker {
    @Composable
    actual fun PickImage(onImagePicked: (ByteArray?) -> Unit) {
        // TODO: Implement iOS image picker
        // Cần sử dụng UIImagePickerController hoặc PHPickerViewController
        // Tạm thời return null
        onImagePicked(null)
    }
}
