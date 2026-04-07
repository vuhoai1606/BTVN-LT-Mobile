package org.example.btvnkotlin.Thi_GK.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.example.btvnkotlin.Thi_GK.data.model.Note
import org.example.btvnkotlin.Thi_GK.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: NoteViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    onNavigateToDetail: (String) -> Unit,  // Thêm callback cho User xem chi tiết
    onLogout: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Note", color = TextWhite, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        // Hiển thị badge Admin hoặc User
                        Surface(
                            color = if (isAdmin) AccentOrange else TextGray,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                                    contentDescription = null,
                                    tint = TextWhite,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    if (isAdmin) "Admin" else "User",
                                    color = TextWhite,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgDarkBlue),
                actions = {
                    IconButton(onClick = { viewModel.logout(onLogout) }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout", tint = AccentOrange)
                    }
                }
            )
        },
        floatingActionButton = {
            // PHÂN QUYỀN: Chỉ Admin mới được thêm note
            if (isAdmin) {
                FloatingActionButton(
                    onClick = onNavigateToAdd,
                    containerColor = AccentOrange,
                    contentColor = TextWhite
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
        },
        containerColor = BgDarkBlue
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Thông báo cho User biết họ chỉ được xem
            if (!isAdmin) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = CardDarkBlue,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = TextGray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Bạn đang ở chế độ xem. Chỉ Admin mới có thể thêm, sửa, xóa.",
                            color = TextGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(notes) { note ->
                    NoteCard(
                        note = note,
                        onClick = {
                            // Admin -> Sửa, User -> Xem chi tiết
                            if (isAdmin) {
                                onNavigateToEdit(note.id)
                            } else {
                                onNavigateToDetail(note.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit
) {
    // Debug: Log image URL
    if (note.file.isNotEmpty()) {
        println("🖼️ NoteCard: Note '${note.title}' has image")
        println("🖼️ NoteCard: URL = ${note.file}")
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = CardDarkBlue),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Hiển thị ảnh thumbnail nếu có
            if (note.file.isNotEmpty() && !note.file.startsWith("local_storage_mock")) {
                KamelImage(
                    resource = asyncPainterResource(note.file),
                    contentDescription = "Note image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    onLoading = {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(CardDarkBlue.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = AccentOrange
                            )
                        }
                    },
                    onFailure = {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(CardDarkBlue.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = TextGray
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            
            // Nội dung note
            Column(modifier = Modifier.weight(1f)) {
                Text(note.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    note.description, 
                    color = TextGray, 
                    fontSize = 14.sp,
                    maxLines = 2
                )
                // Hiển thị icon nếu có ảnh
                if (note.file.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = AccentOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Có hình ảnh", color = AccentOrange, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}