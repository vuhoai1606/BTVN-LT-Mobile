package com.example.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavController) {
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading       by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Text(
                    text = "Tạo Tài Khoản",
                    fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Nhập thông tin để đăng ký",
                    fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(value = email, onValueChange = { email = it },
                    label = { Text("Email") }, placeholder = { Text("example@gmail.com") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    shape = RoundedCornerShape(12.dp))

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(value = password, onValueChange = { password = it },
                    label = { Text("Mật khẩu") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(if (passwordVisible) "Ẩn" else "Hiện", fontSize = 12.sp)
                        }
                    })

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it },
                    label = { Text("Xác nhận mật khẩu") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = confirmPassword.isNotEmpty() && password != confirmPassword,
                    supportingText = {
                        if (confirmPassword.isNotEmpty() && password != confirmPassword)
                            Text("Mật khẩu không khớp", color = Color.Red)
                    })

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        when {
                            email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ->
                                scope.launch { snackbarHostState.showSnackbar("Vui lòng nhập đầy đủ thông tin!") }
                            !email.contains("@") ->
                                scope.launch { snackbarHostState.showSnackbar("Email không hợp lệ!") }
                            password != confirmPassword ->
                                scope.launch { snackbarHostState.showSnackbar("Mật khẩu xác nhận không khớp!") }
                            password.length < 6 ->
                                scope.launch { snackbarHostState.showSnackbar("Mật khẩu phải có ít nhất 6 ký tự!") }
                            else -> {
                                isLoading = true
                                scope.launch {
                                    try {
                                        Firebase.auth.createUserWithEmailAndPassword(email, password)
                                        val createdUser = Firebase.auth.currentUser
                                            ?: error("Không lấy được user vừa tạo")
                                        createdUser.sendEmailVerification()

                                        isLoading = false
                                        snackbarHostState.showSnackbar("Đăng ký thành công! Đã gửi email xác thực.")
                                        delay(1200)
                                        navController.navigate(Screen.Signin.rout) {
                                            popUpTo(Screen.Signup.rout) { inclusive = true }
                                        }
                                    } catch (e: Exception) {
                                        isLoading = false
                                        snackbarHostState.showSnackbar(e.message ?: "Đăng ký thất bại")
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Đang đăng ký...", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Text("Đăng Ký", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Đã có tài khoản?", fontSize = 14.sp, color = Color.Gray)
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Đăng Nhập", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                    }
                }
                }
            }
        }
    }
}
