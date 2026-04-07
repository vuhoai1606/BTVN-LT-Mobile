package org.example.btvnkotlin.Thi_GK.data.cloudinary

/**
 * CloudinaryUploader - Expect/actual pattern để upload multiplatform
 * Android sử dụng Cloudinary SDK, các platform khác dùng HTTP
 */
expect object CloudinaryUploader {
    /**
     * Upload ảnh đã được chọn bởi ImagePicker
     * @return URL của ảnh đã upload hoặc empty string nếu thất bại
     */
    suspend fun uploadLastPickedImage(): String
}
