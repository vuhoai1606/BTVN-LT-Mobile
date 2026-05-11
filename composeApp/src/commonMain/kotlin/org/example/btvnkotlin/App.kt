package org.example.btvnkotlin

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.authentication.MyNavigation
import com.example.authentication.ui.theme.Lab8AuthTheme
 import org.example.btvnkotlin.Week6_03_17.CupcakeApp
 import org.example.btvnkotlin.Week6_03_17.ui.theme.CupcakeTheme
 import org.example.btvnkotlin.Week9_04_14.ui.Week9WorkManagerScreen

@Composable
@Preview
fun App() {
    // Dòng này dùng cho lab 1, 2, 3, 4
//    MaterialTheme {
//        WoofApp()
//    }

//    // Dòng này dùng cho lab 5
//    WoofApp()

//    // Dòng này dùng cho lab 6
//    CupcakeTheme {
//        CupcakeApp()
//    }

    // Dòng này dùng cho lab 8
    Lab8AuthTheme {
        MyNavigation()
    }

//    // Dòng này dùng cho Thi GK
//    val navController = rememberNavController()
//    // Khởi tạo ViewModel duy nhất dùng chung cho toàn bộ App
//    val viewModel: NoteViewModel = viewModel { NoteViewModel() }
//
//    NavHost(navController = navController, startDestination = "login") {
//
//        composable("login") {
//            LoginScreen(
//                viewModel = viewModel,
//                onLoginSuccess = {
//                    navController.navigate("home") { popUpTo("login") { inclusive = true } }
//                },
//                onNavigateToRegister = {
//                    navController.navigate("register")
//                }
//            )
//        }
//
//        composable("register") {
//            RegisterScreen(
//                viewModel = viewModel,
//                onRegisterSuccess = {
//                    // Sau khi đăng ký thành công, chuyển thẳng đến home
//                    navController.navigate("home") {
//                        popUpTo("login") { inclusive = true }
//                    }
//                },
//                onNavigateToLogin = {
//                    navController.popBackStack()
//                }
//            )
//        }
//
//        composable("home") {
//            HomeScreen(
//                viewModel = viewModel,
//                onNavigateToAdd = {
//                    // 👉 TRƯỚC KHI CHUYỂN MÀN HÌNH: Xóa ID cũ đi để nó hiểu là Thêm mới
//                    viewModel.selectedNoteId = null
//                    navController.navigate("addEdit")
//                },
//                onNavigateToEdit = { noteId ->
//                    // 👉 Admin: Nhét cái ID vào ViewModel để màn sửa lấy xài
//                    viewModel.selectedNoteId = noteId
//                    navController.navigate("addEdit")
//                },
//                onNavigateToDetail = { noteId ->
//                    // 👉 User: Xem chi tiết (chỉ đọc)
//                    viewModel.selectedNoteId = noteId
//                    navController.navigate("detail")
//                },
//                onLogout = {
//                    navController.navigate("login") { popUpTo("home") { inclusive = true } }
//                }
//            )
//        }
//
//        // 👉 Màn hình xem chi tiết (dành cho User)
//        composable("detail") {
//            NoteDetailScreen(
//                noteId = viewModel.selectedNoteId ?: "",
//                viewModel = viewModel,
//                onBack = { navController.popBackStack() }
//            )
//        }
//
//        // 👉 TUYẾN ĐƯỜNG ĐÃ ĐƯỢC TỐI GIẢN: Không cần truyền tham số {noteId} nhức đầu nữa!
//        composable("addEdit") {
//            AddEditScreen(
//                noteId = viewModel.selectedNoteId, // Lấy ID trực tiếp từ ViewModel ra xài
//                viewModel = viewModel,
//                onBack = { navController.popBackStack() }
//            )
//        }
//    }

//    // Dòng này cho lab 9 NHƯNG MÀ LAB 9 KMP KO DÙNG ĐƯỢC NÊN CHUYỂN SANG ANDROID STUDIO
//    Week9WorkManagerScreen()
}
