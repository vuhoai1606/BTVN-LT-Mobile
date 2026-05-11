package com.example.authentication

// Sealed class định nghĩa các màn hình trong ứng dụng
sealed class Screen(val rout: String) {
    object Signin : Screen("signin_screen")
    object Signup : Screen("signup_screen")
    object Home   : Screen("home_screen")
}