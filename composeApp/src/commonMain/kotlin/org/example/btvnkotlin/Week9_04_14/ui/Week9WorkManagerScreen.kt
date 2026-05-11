package org.example.btvnkotlin.Week9_04_14.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.example.btvnkotlin.Week9_04_14.data.BlurAmountData
import org.example.btvnkotlin.Week9_04_14.data.rememberWeek9BlurRepository

@Composable
fun Week9WorkManagerScreen(modifier: Modifier = Modifier) {
    val repository = rememberWeek9BlurRepository()
    val blurUiState by repository.blurUiState.collectAsState()
    var selectedAmount by rememberSaveable { mutableIntStateOf(1) }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Week9_04_14 - Blur bằng WorkManager (KMP)",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Nguồn logic: composeApp/workmanager (Blur-O-Matic)",
                style = MaterialTheme.typography.bodyMedium
            )

            Column(modifier = Modifier.selectableGroup()) {
                BlurAmountData.options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedAmount == option.amount,
                                role = Role.RadioButton,
                                onClick = { selectedAmount = option.amount }
                            )
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedAmount == option.amount,
                            onClick = null
                        )
                        Text(text = option.label, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { repository.applyBlur(selectedAmount) },
                    modifier = Modifier.weight(1f),
                    enabled = blurUiState !is BlurUiState.Loading
                ) {
                    Text("Start")
                }

                Button(
                    onClick = repository::cancelWork,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
            }

            val statusText = remember(blurUiState) {
                when (val current = blurUiState) {
                    BlurUiState.Idle -> "Trạng thái: Chờ thao tác"
                    BlurUiState.Loading -> "Trạng thái: Đang xử lý nền..."
                    is BlurUiState.Completed -> "Xong. File ảnh blur: ${current.outputUri}"
                    is BlurUiState.Error -> "Lỗi: ${current.message}"
                }
            }
            Text(text = statusText, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

