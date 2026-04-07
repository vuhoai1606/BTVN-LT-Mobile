package org.example.btvnkotlin.Thi_GK.util

import androidx.compose.runtime.Composable
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

/**
 * JVM/Desktop implementation của ImagePicker
 * Sử dụng Swing JFileChooser
 */
actual object ImagePicker {
    @Composable
    actual fun PickImage(onImagePicked: (ByteArray?) -> Unit) {
        val fileChooser = JFileChooser()
        fileChooser.fileFilter = FileNameExtensionFilter(
            "Image files", 
            "jpg", "jpeg", "png", "gif", "bmp"
        )
        
        val result = fileChooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                val file: File = fileChooser.selectedFile
                val bytes = file.readBytes()
                onImagePicked(bytes)
            } catch (e: Exception) {
                e.printStackTrace()
                onImagePicked(null)
            }
        } else {
            onImagePicked(null)
        }
    }
}
