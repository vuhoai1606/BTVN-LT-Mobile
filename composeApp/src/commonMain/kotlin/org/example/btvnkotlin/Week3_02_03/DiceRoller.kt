package org.example.btvnkotlin.Week3_02_03

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button // Dùng Material3 cho đồng bộ với App.kt
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Import Resource từ thư mục generated
import btvnkotlin.composeapp.generated.resources.Res
import btvnkotlin.composeapp.generated.resources.dice_1
import btvnkotlin.composeapp.generated.resources.dice_2
import btvnkotlin.composeapp.generated.resources.dice_3
import btvnkotlin.composeapp.generated.resources.dice_4
import btvnkotlin.composeapp.generated.resources.dice_5
import btvnkotlin.composeapp.generated.resources.dice_6
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

@Composable
fun DiceRoller() {
    // Trạng thái lưu kết quả xúc sắc
    var result by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageResource = when (result) {
            1 -> Res.drawable.dice_1
            2 -> Res.drawable.dice_2
            3 -> Res.drawable.dice_3
            4 -> Res.drawable.dice_4
            5 -> Res.drawable.dice_5
            else -> Res.drawable.dice_6
        }

        Image(
            painter = painterResource(imageResource),
            contentDescription = "Dice $result",
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { result = Random.nextInt(1, 7) }) {
            Text(text = "Tung xúc sắc", fontSize = 18.sp)
        }
    }
}