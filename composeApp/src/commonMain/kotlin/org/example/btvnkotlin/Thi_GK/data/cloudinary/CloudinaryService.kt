package org.example.btvnkotlin.Thi_GK.data.cloudinary

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * CloudinaryService - Dịch vụ upload ảnh lên Cloudinary
 * Sử dụng UNSIGNED upload preset
 */
class CloudinaryService(
    private val cloudName: String,
    private val uploadPreset: String
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
    }

    /**
     * Upload ảnh lên Cloudinary
     */
    suspend fun uploadImage(
        imageBytes: ByteArray,
        fileName: String? = null
    ): CloudinaryUploadResult {
        return try {
            println("🔵 Cloudinary: Starting upload...")
            println("🔵 Cloudinary: File size: ${imageBytes.size} bytes")
            println("🔵 Cloudinary: Cloud Name: $cloudName")
            println("🔵 Cloudinary: Upload Preset: $uploadPreset")
            
            val uploadUrl = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"
            
            // Upload using multipart form - FIX: Correct way to send form data
            val response: HttpResponse = client.post(uploadUrl) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            // Text fields first
                            append(
                                key = "upload_preset",
                                value = uploadPreset
                            )
                            append(
                                key = "folder",
                                value = "notes"
                            )
                            // File field
                            append(
                                key = "file",
                                value = imageBytes,
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpeg")
                                    append(HttpHeaders.ContentDisposition, "filename=\"${fileName ?: "image.jpg"}\"")
                                }
                            )
                        }
                    )
                )
            }
            
            val responseText = response.bodyAsText()
            println("🔵 Cloudinary: Response status: ${response.status}")
            println("🔵 Cloudinary: Response body: $responseText")
            
            if (response.status.isSuccess()) {
                val result = json.decodeFromString<CloudinaryUploadResponse>(responseText)
                println("✅ Cloudinary: Upload SUCCESS!")
                println("✅ Cloudinary: URL: ${result.secureUrl}")
                
                CloudinaryUploadResult.Success(
                    url = result.secureUrl,
                    publicId = result.publicId,
                    thumbnailUrl = result.secureUrl.replace("/upload/", "/upload/c_fill,h_150,w_150/")
                )
            } else {
                println("❌ Cloudinary: HTTP Error ${response.status}")
                CloudinaryUploadResult.Error("HTTP ${response.status}: $responseText")
            }
        } catch (e: Exception) {
            println("❌ Cloudinary: Exception: ${e.message}")
            e.printStackTrace()
            CloudinaryUploadResult.Error(e.message ?: "Unknown error")
        }
    }

    fun close() {
        client.close()
    }
}

@Serializable
data class CloudinaryUploadResponse(
    @SerialName("secure_url")
    val secureUrl: String = "",
    
    @SerialName("public_id")
    val publicId: String = "",
    
    @SerialName("url")
    val url: String = ""
)

sealed class CloudinaryUploadResult {
    data class Success(
        val url: String,
        val publicId: String,
        val thumbnailUrl: String
    ) : CloudinaryUploadResult()
    
    data class Error(
        val message: String
    ) : CloudinaryUploadResult()
}
