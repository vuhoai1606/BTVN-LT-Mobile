package org.example.btvnkotlin.Thi_GK.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.btvnkotlin.Thi_GK.data.model.Note
import org.example.btvnkotlin.Thi_GK.data.repository.AdminRepository

class NoteViewModel : ViewModel() {
    private val repository = AdminRepository()

    // Biến lưu trữ ID của Note đang được chọn để Sửa
    var selectedNoteId: String? = null

    // Danh sách Note lấy từ Firebase
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    // Trạng thái Admin (Phân quyền 1 điểm)
    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    // Trạng thái load dữ liệu
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    // Trạng thái upload ảnh
    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading

    init {
        checkAuthStatus()
        loadNotes()
    }

    fun checkAuthStatus() {
        _isAdmin.value = repository.isCurrentUserAdmin()
    }

    // ĐỌC DỮ LIỆU
    private fun loadNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllNotes().collect { noteList ->
                _notes.value = noteList
                _isLoading.value = false
            }
        }
    }

    // ĐĂNG NHẬP
    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val success = repository.login(email, pass)
            _isLoading.value = false
            if (success) {
                checkAuthStatus()
                onSuccess()
            } else {
                onError()
            }
        }
    }
    
    // ĐĂNG KÝ
    fun register(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val (success, errorMessage) = repository.register(email, password)
            _isLoading.value = false
            if (success) {
                checkAuthStatus()
                onSuccess()
            } else {
                onError(errorMessage ?: "Đăng ký thất bại")
            }
        }
    }

    // ĐĂNG XUẤT
    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            checkAuthStatus()
            onSuccess()
        }
    }

    // THÊM NOTE (Kèm Upload File)
    // hasImage: true nếu người dùng đã chọn ảnh từ ImagePicker
    fun addNote(title: String, description: String, hasImage: Boolean, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                println("🎯 ViewModel: addNote called")
                println("🎯 ViewModel: Title: $title")
                println("🎯 ViewModel: Has image: $hasImage")
                
                _isLoading.value = true
                _isUploading.value = true
                var fileUrl = ""
                
                // Nếu có file đính kèm thì upload lên Cloudinary
                if (hasImage) {
                    println("🎯 ViewModel: Starting Cloudinary upload...")
                    fileUrl = repository.uploadSelectedImage()
                    println("🎯 ViewModel: Upload completed. URL: $fileUrl")
                    
                    if (fileUrl.isEmpty()) {
                        println("❌ ViewModel: Upload failed! URL is empty")
                    }
                } else {
                    println("⚠️ ViewModel: No image to upload")
                }
                
                _isUploading.value = false
                
                println("🎯 ViewModel: Saving to Firestore with URL: $fileUrl")
                repository.addNote(title, description, fileUrl)
                
                _isLoading.value = false
                println("✅ ViewModel: Note saved successfully!")
                onComplete()
            } catch (e: Exception) {
                println("❌ ViewModel: Error adding note: ${e.message}")
                e.printStackTrace()
                _isUploading.value = false
                _isLoading.value = false
            }
        }
    }

    // CẬP NHẬT NOTE (hỗ trợ thay đổi ảnh)
    fun updateNote(
        id: String,
        title: String,
        description: String,
        currentImageUrl: String,
        hasNewImage: Boolean,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                println("🎯 ViewModel: updateNote called")
                println("🎯 ViewModel: ID: $id")
                println("🎯 ViewModel: Title: $title")
                println("🎯 ViewModel: Has new image: $hasNewImage")
                
                _isLoading.value = true
                _isUploading.value = hasNewImage
                
                var finalImageUrl = currentImageUrl
                
                // Nếu có ảnh mới thì upload và thay thế URL cũ
                if (hasNewImage) {
                    println("🎯 ViewModel: Uploading NEW image...")
                    val newUrl = repository.uploadSelectedImage()
                    if (newUrl.isNotEmpty()) {
                        finalImageUrl = newUrl
                        println("✅ ViewModel: New image uploaded: $newUrl")
                    } else {
                        println("❌ ViewModel: New image upload failed, keeping old image")
                    }
                }
                
                _isUploading.value = false
                
                println("🎯 ViewModel: Updating Firestore with URL: $finalImageUrl")
                repository.updateNote(id, title, description, finalImageUrl)
                
                _isLoading.value = false
                println("✅ ViewModel: Note updated successfully!")
                onComplete()
            } catch (e: Exception) {
                println("❌ ViewModel: Error updating note: ${e.message}")
                e.printStackTrace()
                _isUploading.value = false
                _isLoading.value = false
            }
        }
    }

    // XÓA NOTE
    fun deleteNote(id: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.deleteNote(id)
            _isLoading.value = false
            onComplete()
        }
    }
}