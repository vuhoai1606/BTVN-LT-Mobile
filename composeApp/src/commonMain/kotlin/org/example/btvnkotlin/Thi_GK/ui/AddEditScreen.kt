package org.example.btvnkotlin.Thi_GK.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.example.btvnkotlin.Thi_GK.ui.theme.*
import org.example.btvnkotlin.Thi_GK.util.ImagePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    noteId: String?, // Nếu null là Thêm mới, nếu có ID là Cập nhật
    viewModel: NoteViewModel,
    onBack: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val isEditMode = noteId != null

    // Tìm Note hiện tại nếu đang ở chế độ Edit
    val currentNote = if (isEditMode) notes.find { it.id == noteId } else null

    var title by remember { mutableStateOf(currentNote?.title ?: "") }
    var description by remember { mutableStateOf(currentNote?.description ?: "") }
    var currentImageUrl by remember { mutableStateOf(currentNote?.file ?: "") }

    // Biến đánh dấu đã chọn ảnh MỚI từ ImagePicker (dùng cho cả add và edit)
    var hasSelectedNewImage by remember { mutableStateOf(false) }
    var selectedImageBytes by remember { mutableStateOf<ByteArray?>(null) }
    
    // State để trigger image picker
    var showImagePicker by remember { mutableStateOf(false) }
    
    // State cho upload progress
    val isUploading by viewModel.isUploading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Sửa Note" else "Thêm Note", color = TextWhite) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDarkBlue),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = AccentOrange)
                    }
                },
                actions = {
                    // Nút XÓA góc phải (giống hệt trong video)
                    if (isEditMode) {
                        IconButton(onClick = { viewModel.deleteNote(noteId!!) { onBack() } }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                }
            )
        },
        containerColor = BgDarkBlue
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            Text("Title", color = TextWhite, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentOrange,
                    unfocusedBorderColor = TextGray,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Description", color = TextWhite, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentOrange,
                    unfocusedBorderColor = TextGray,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                modifier = Modifier.fillMaxWidth().height(150.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            // YÊU CẦU 2 (1 ĐIỂM): Giao diện chọn và upload ảnh lên Cloudinary
            Text("Hình ảnh đính kèm", color = TextWhite, fontWeight = FontWeight.Bold)
            
            // Hiển thị ảnh: ưu tiên ảnh mới chọn, nếu không thì hiển thị ảnh cũ
            if (hasSelectedNewImage) {
                // Hiển thị thông báo ảnh MỚI đã chọn (chưa upload)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = CardDarkBlue)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = AccentOrange
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                "✅ Ảnh MỚI đã được chọn",
                                color = AccentOrange,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Kích thước: ${(selectedImageBytes?.size ?: 0) / 1024} KB",
                                color = TextWhite.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            if (isEditMode && currentImageUrl.isNotEmpty()) {
                                Text(
                                    "Ảnh cũ sẽ được thay thế khi lưu",
                                    color = TextGray,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            } else if (currentImageUrl.isNotEmpty() && !currentImageUrl.startsWith("local_storage_mock")) {
                // Hiển thị ảnh hiện tại từ Cloudinary
                KamelImage(
                    resource = asyncPainterResource(currentImageUrl),
                    contentDescription = "Note image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp),
                    contentScale = ContentScale.Crop,
                    onLoading = { CircularProgressIndicator() },
                    onFailure = { 
                        Text("Không thể tải ảnh", color = Color.Red) 
                    }
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showImagePicker = true },
                    colors = ButtonDefaults.buttonColors(containerColor = CardDarkBlue),
                    enabled = !isUploading
                ) {
                    Icon(
                        imageVector = if (isEditMode && currentImageUrl.isNotEmpty()) Icons.Default.Refresh else Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (isEditMode && currentImageUrl.isNotEmpty()) "Đổi Ảnh" else "Chọn Ảnh",
                        color = TextWhite
                    )
                }
                
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = AccentOrange
                    )
                    Text(
                        "Đang upload...",
                        color = AccentOrange,
                        modifier = Modifier.padding(start = 8.dp, top = 12.dp)
                    )
                }
            }
            
            // Image Picker
            if (showImagePicker) {
                ImagePicker.PickImage { imageBytes: ByteArray? ->
                    println("📸 AddEditScreen: ImagePicker callback, bytes = ${imageBytes?.size ?: "null"}")
                    if (imageBytes != null) {
                        hasSelectedNewImage = true
                        selectedImageBytes = imageBytes
                        println("📸 AddEditScreen: NEW image selected! Size = ${imageBytes.size} bytes")
                    } else {
                        println("⚠️ AddEditScreen: No image selected")
                    }
                    showImagePicker = false
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Đẩy nút bấm xuống cuối màn hình giống video

            Button(
                onClick = {
                    println("🔘 AddEditScreen: Save button clicked")
                    println("🔘 AddEditScreen: hasSelectedNewImage = $hasSelectedNewImage")
                    println("🔘 AddEditScreen: isEditMode = $isEditMode")
                    
                    if (isEditMode) {
                        // Chế độ Edit: Cập nhật note, có thể thay đổi ảnh
                        viewModel.updateNote(
                            id = noteId!!,
                            title = title,
                            description = description,
                            currentImageUrl = currentImageUrl,
                            hasNewImage = hasSelectedNewImage
                        ) { onBack() }
                    } else {
                        // Chế độ Add: Thêm mới note
                        viewModel.addNote(title, description, hasSelectedNewImage) { onBack() }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = !isUploading
            ) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = TextWhite,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(if (isEditMode) "UPDATE ITEM" else "ADD ITEM", color = TextWhite, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}