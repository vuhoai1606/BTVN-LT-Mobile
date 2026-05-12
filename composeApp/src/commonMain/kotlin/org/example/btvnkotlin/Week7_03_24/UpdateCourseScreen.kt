package org.example.btvnkotlin.Week7_03_24

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.btvnkotlin.Week7_03_24.model.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCourseScreen(course: Course, onNavigateToList: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var courseName by remember { mutableStateOf(course.courseName) }
    var courseDuration by remember { mutableStateOf(course.courseDuration) }
    var courseDescription by remember { mutableStateOf(course.courseDescription) }
    var statusMessage by remember { mutableStateOf("") }
    var isSuccessMessage by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cập Nhật Dữ Liệu", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(value = courseName, onValueChange = { courseName = it }, placeholder = { Text("Tên khóa học") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            TextField(value = courseDuration, onValueChange = { courseDuration = it }, placeholder = { Text("Thời gian học") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            TextField(value = courseDescription, onValueChange = { courseDescription = it }, placeholder = { Text("Mô tả khóa học") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = statusMessage,
                color = if (isSuccessMessage) Color(0xFF2E7D32) else Color.Red
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (courseName.isEmpty() || courseDuration.isEmpty() || courseDescription.isEmpty()) {
                        isSuccessMessage = false
                        statusMessage = "Vui lòng nhập đầy đủ thông tin!"
                    } else {
                        coroutineScope.launch {
                            try {
                                val updatedCourse = Course(course.courseID, courseName, courseDuration, courseDescription)
                                Firebase.firestore.collection("Courses").document(course.courseID).set(updatedCourse)
                                isSuccessMessage = true
                                statusMessage = "Cập nhật thành công! Đang chuyển sang danh sách..."
                                delay(900)
                                onNavigateToList()
                            } catch (e: Exception) {
                                isSuccessMessage = false
                                statusMessage = "Lỗi cập nhật: ${e.message}"
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Update Data") }

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            Firebase.firestore.collection("Courses").document(course.courseID).delete()
                            isSuccessMessage = true
                            statusMessage = "Xóa khóa học thành công! Đang chuyển sang danh sách..."
                            delay(900)
                            onNavigateToList()
                        } catch (e: Exception) {
                            isSuccessMessage = false
                            statusMessage = "Lỗi xóa: ${e.message}"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Delete Course")
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onNavigateToList,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Hủy bỏ")
            }
        }
    }
}
