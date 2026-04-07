package org.example.btvnkotlin

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

import org.example.btvnkotlin.Week2_01_24.BusinessCard
import org.example.btvnkotlin.Week3_02_03.DiceRoller
import org.example.btvnkotlin.Week4_03_03.TipTime
import org.example.btvnkotlin.Week5_03_10.WoofApp
import org.example.btvnkotlin.Week6_03_17.CupcakeApp
import org.example.btvnkotlin.Week6_03_17.ui.theme.CupcakeTheme

// ========== phần này dùng cho THI GK ==========
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.btvnkotlin.Thi_GK.ui.AddEditScreen
import org.example.btvnkotlin.Thi_GK.ui.HomeScreen
import org.example.btvnkotlin.Thi_GK.ui.LoginScreen
import org.example.btvnkotlin.Thi_GK.ui.RegisterScreen
import org.example.btvnkotlin.Thi_GK.ui.NoteDetailScreen
import org.example.btvnkotlin.Thi_GK.ui.NoteViewModel
// ==============================================

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

    // Dòng này dùng cho Thi GK
    val navController = rememberNavController()
    // Khởi tạo ViewModel duy nhất dùng chung cho toàn bộ App
    val viewModel: NoteViewModel = viewModel { NoteViewModel() }

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate("home") { popUpTo("login") { inclusive = true } }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        
        composable("register") {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    // Sau khi đăng ký thành công, chuyển thẳng đến home
                    navController.navigate("home") { 
                        popUpTo("login") { inclusive = true } 
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToAdd = {
                    // 👉 TRƯỚC KHI CHUYỂN MÀN HÌNH: Xóa ID cũ đi để nó hiểu là Thêm mới
                    viewModel.selectedNoteId = null
                    navController.navigate("addEdit")
                },
                onNavigateToEdit = { noteId ->
                    // 👉 Admin: Nhét cái ID vào ViewModel để màn sửa lấy xài
                    viewModel.selectedNoteId = noteId
                    navController.navigate("addEdit")
                },
                onNavigateToDetail = { noteId ->
                    // 👉 User: Xem chi tiết (chỉ đọc)
                    viewModel.selectedNoteId = noteId
                    navController.navigate("detail")
                },
                onLogout = {
                    navController.navigate("login") { popUpTo("home") { inclusive = true } }
                }
            )
        }
        
        // 👉 Màn hình xem chi tiết (dành cho User)
        composable("detail") {
            NoteDetailScreen(
                noteId = viewModel.selectedNoteId ?: "",
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // 👉 TUYẾN ĐƯỜNG ĐÃ ĐƯỢC TỐI GIẢN: Không cần truyền tham số {noteId} nhức đầu nữa!
        composable("addEdit") {
            AddEditScreen(
                noteId = viewModel.selectedNoteId, // Lấy ID trực tiếp từ ViewModel ra xài
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}