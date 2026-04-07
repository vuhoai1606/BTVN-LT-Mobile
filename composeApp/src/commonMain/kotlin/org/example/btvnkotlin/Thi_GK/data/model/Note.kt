package org.example.btvnkotlin.Thi_GK.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: String = "",          // ID ngẫu nhiên của Firebase Document
    val title: String = "",       // Tiêu đề
    val description: String = "", // Mô tả
    val file: String = ""         // Lưu URL của file/hình ảnh sau khi up lên Storage
)