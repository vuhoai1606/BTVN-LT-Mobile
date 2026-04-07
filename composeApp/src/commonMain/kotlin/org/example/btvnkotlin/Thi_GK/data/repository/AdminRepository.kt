package org.example.btvnkotlin.Thi_GK.data.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.btvnkotlin.Thi_GK.data.model.Note
import org.example.btvnkotlin.Thi_GK.data.cloudinary.CloudinaryUploader

class AdminRepository {
    // Trỏ tới bảng "Notes" trong Cloud Firestore
    private val db = Firebase.firestore.collection("Notes")
    // Trỏ tới dịch vụ Xác thực
    private val auth = Firebase.auth

    // ==========================================
    // ĐĂNG NHẬP, ĐĂNG KÝ, ĐĂNG XUẤT, PHÂN QUYỀN
    // ==========================================

    suspend fun login(email: String, pass: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, pass)
            true
        } catch (e: Exception) {
            false // Đăng nhập thất bại
        }
    }
    
    /**
     * Đăng ký tài khoản mới
     * @return Pair<Boolean, String?> - (thành công, thông báo lỗi nếu có)
     */
    suspend fun register(email: String, pass: String): Pair<Boolean, String?> {
        return try {
            auth.createUserWithEmailAndPassword(email, pass)
            Pair(true, null)
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("email-already-in-use") == true -> 
                    "Email này đã được sử dụng"
                e.message?.contains("weak-password") == true -> 
                    "Mật khẩu quá yếu"
                e.message?.contains("invalid-email") == true -> 
                    "Email không hợp lệ"
                else -> e.message ?: "Đăng ký thất bại"
            }
            Pair(false, errorMessage)
        }
    }

    suspend fun logout() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            println("Lỗi đăng xuất: ${e.message}")
        }
    }

    // Phân quyền: Kiểm tra xem user hiện tại có phải Admin không
    fun isCurrentUserAdmin(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null && currentUser.email?.contains("admin") == true
    }

    // ==========================================
    // CRUD - THÊM, ĐỌC, SỬA, XÓA
    // ==========================================

    // ĐỌC: Trả về dòng chảy dữ liệu (Flow) realtime
    fun getAllNotes(): Flow<List<Note>> {
        return db.snapshots.map { querySnapshot ->
            querySnapshot.documents.map { document ->
                // Ép kiểu JSON từ Firebase thành class Note của chúng ta
                val note = document.data<Note>()
                // Gắn cái ID thực tế của Firebase vào thuộc tính id của Note
                note.copy(id = document.id)
            }
        }
    }

    // THÊM (CREATE)
    suspend fun addNote(title: String, description: String, fileUrl: String) {
        println("💾 Repository: Adding note to Firestore")
        println("💾 Repository: Title: $title")
        println("💾 Repository: File URL: $fileUrl")
        
        val newNote = Note(
            title = title,
            description = description,
            file = fileUrl
        )
        // Firebase tự động tạo ID document
        db.add(newNote)
        println("✅ Repository: Note added to Firestore successfully!")
    }

    // SỬA (UPDATE)
    suspend fun updateNote(noteId: String, newTitle: String, newDescription: String, newFileUrl: String) {
        val updatedNote = Note(
            title = newTitle,
            description = newDescription,
            file = newFileUrl
        )
        // Tìm đúng document có id đó và ghi đè
        db.document(noteId).set(updatedNote)
    }

    // XÓA (DELETE)
    suspend fun deleteNote(noteId: String) {
        db.document(noteId).delete()
    }

    // ==========================================
    // UPLOAD ẢNH LÊN CLOUDINARY
    // ==========================================

    /**
     * Upload ảnh đã được chọn bởi ImagePicker lên Cloudinary
     * Sử dụng CloudinaryUploader (expect/actual) để hỗ trợ multiplatform
     * Android dùng Cloudinary SDK, các platform khác dùng HTTP
     */
    suspend fun uploadSelectedImage(): String {
        println("📤 Repository: Starting Cloudinary upload...")
        val url = CloudinaryUploader.uploadLastPickedImage()
        if (url.isNotEmpty()) {
            println("✅ Repository: Upload successful! URL: $url")
        } else {
            println("❌ Repository: Upload failed!")
        }
        return url
    }
}