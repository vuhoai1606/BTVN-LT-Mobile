package org.example.btvnkotlin.Week2_01_24

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import btvnkotlin.composeapp.generated.resources.Res
import btvnkotlin.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

// QUAN TRỌNG: Xóa import Res thủ công, hãy để IntelliJ tự gợi ý sau khi Build
// import org.jetbrains.compose.resources.Res

@Composable
fun BusinessCard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2E8D4))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- PHẦN TRÊN: Thông tin cá nhân ---
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Trong KMP, chúng ta dùng painterResource của org.jetbrains.compose
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFF073042))
            ) {
                Image(
                    painter = painterResource(Res.drawable.logo), // Link tới file ảnh trong commonMain/composeResources
                    contentDescription = "Logo",
                    modifier = Modifier.fillMaxSize().padding(0.dp)
                )
            }

            Text(
                text = "Vux",
                fontSize = 40.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = "Student of VKU",
                fontSize = 14.sp,
                color = Color(0xFF006B3C),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // --- PHẦN DƯỚI: Thông tin liên hệ ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ContactRow(icon = Icons.Default.Phone, text = "+84 012 345 6789")
            ContactRow(icon = Icons.Default.Share, text = "@THV")
            ContactRow(icon = Icons.Default.Email, text = "abc@gmail.com")
        }
    }
}

@Composable
fun ContactRow(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .width(260.dp)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF006B3C),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = text,
            fontSize = 15.sp,
            color = Color.Black
        )
    }
}