package com.example.authentication

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MyNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Signin.rout
    ) {
        composable(Screen.Signin.rout) {
            SignInScreen(navController = navController)
        }
        composable(Screen.Signup.rout) {
            SignUpScreen(navController = navController)
        }
        composable(Screen.Home.rout) {
            HomeScreen(navController = navController)
        }
    }
}