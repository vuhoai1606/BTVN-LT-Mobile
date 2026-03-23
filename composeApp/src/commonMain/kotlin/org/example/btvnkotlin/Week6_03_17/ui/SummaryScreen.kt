package org.example.btvnkotlin.Week6_03_17.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import btvnkotlin.composeapp.generated.resources.*
import org.example.btvnkotlin.Week6_03_17.data.OrderUiState
import org.example.btvnkotlin.Week6_03_17.ui.components.FormattedPriceLabel
import org.jetbrains.compose.resources.stringResource

@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit = {},
    onSendButtonClicked: (String, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    // KMP: Tạm thời chúng ta dùng định dạng chuỗi đơn giản thay vì Plurals phức tạp của Android
    val numberOfCupcakes = "${orderUiState.quantity} cupcakes"

    // TẠO NỘI DUNG ĐƠN HÀNG ĐỂ GỬI ĐI
    val orderSubject = "Đơn hàng Cupcake mới"
    val orderSummary = "Số lượng: $numberOfCupcakes \n" +
            "Hương vị: ${orderUiState.flavor} \n" +
            "Ngày lấy: ${orderUiState.date} \n" +
            "Tổng tiền: ${orderUiState.price}"

    val items = listOf(
        Pair(stringResource(Res.string.quantity), numberOfCupcakes),
        Pair(stringResource(Res.string.flavor), orderUiState.flavor),
        Pair(stringResource(Res.string.pickup_date), orderUiState.date)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                Text(item.first.uppercase())
                Text(text = item.second, fontWeight = FontWeight.Bold)
                Divider(thickness = 1.dp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            FormattedPriceLabel(
                subtotal = orderUiState.price,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    // Bấm nút thì đẩy tiêu đề và nội dung lên NavHost
                    onClick = { onSendButtonClicked(orderSubject, orderSummary) }
                ) {
                    Text(stringResource(Res.string.send))
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(Res.string.cancel))
                }
            }
        }
    }
}