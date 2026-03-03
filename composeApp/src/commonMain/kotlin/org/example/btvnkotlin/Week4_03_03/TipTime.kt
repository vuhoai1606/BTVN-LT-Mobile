package org.example.btvnkotlin.Week4_03_03

import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TipTime() {
    MaterialTheme {
        // 1. Khai báo các State (Trạng thái)
        // Dùng 'var ... by remember' để Compose ghi nhớ giá trị khi giao diện vẽ lại
        var amountInput by remember { mutableStateOf("") }
        var tipInput by remember { mutableStateOf("15") }

        // Chuyển đổi String từ ô nhập liệu sang Double để tính toán
        val amount = amountInput.toDoubleOrNull() ?: 0.0
        val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
        val tip = calculateTip(amount, tipPercent)

        // 2. Bố cục giao diện (Column sắp xếp các phần tử theo chiều dọc)
        Column(
            modifier = Modifier.fillMaxSize().padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Tính tiền Tip", fontSize = 24.sp)

            Spacer(Modifier.height(16.dp))

            // Ô nhập số tiền dịch vụ
            TextField(
                value = amountInput,
                onValueChange = { amountInput = it }, // Cập nhật state khi người dùng gõ
                label = { Text("Số tiền dịch vụ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Ô nhập phần trăm Tip
            TextField(
                value = tipInput,
                onValueChange = { tipInput = it },
                label = { Text("% Tip") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Hiển thị kết quả
            Text(
                text = "Số tiền boa: $tip",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
