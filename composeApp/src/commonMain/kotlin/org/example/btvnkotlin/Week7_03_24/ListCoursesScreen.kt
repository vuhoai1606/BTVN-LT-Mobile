package org.example.btvnkotlin.Week7_03_24

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.launch
import org.example.btvnkotlin.Week7_03_24.model.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListCoursesScreen(onNavigateToAdd: () -> Unit, onNavigateToUpdate: (Course) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var courseList by remember { mutableStateOf(listOf<Course>()) }
    var statusMessage by remember { mutableStateOf("Đang tải dữ liệu...") }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val snapshot = Firebase.firestore.collection("Courses").get()
                val list = snapshot.documents.map { it.data<Course>() }
                if (list.isNotEmpty()) {
                    courseList = list
                    statusMessage = ""
                } else {
                    statusMessage = "Không có dữ liệu khóa học."
                }
            } catch (e: Exception) {
                statusMessage = "Lỗi lấy dữ liệu: ${e.message}"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh sách khóa học", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Button(onClick = onNavigateToAdd, modifier = Modifier.padding(16.dp)) { Text("Quay lại thêm mới") }

            if (statusMessage.isNotEmpty()) {
                Text(text = statusMessage, modifier = Modifier.padding(16.dp))
            }

            LazyColumn {
                items(courseList) { course ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable { onNavigateToUpdate(course) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = course.courseName, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = "Thời gian: ${course.courseDuration}", style = TextStyle(fontSize = 15.sp))
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = "Mô tả: ${course.courseDescription}", style = TextStyle(fontSize = 15.sp))
                        }
                    }
                }
            }
        }
    }
}