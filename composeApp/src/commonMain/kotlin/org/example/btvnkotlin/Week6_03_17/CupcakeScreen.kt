package org.example.btvnkotlin.Week6_03_17

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import btvnkotlin.composeapp.generated.resources.*
import org.example.btvnkotlin.Week6_03_17.data.DataSource
import org.example.btvnkotlin.Week6_03_17.ui.OrderSummaryScreen
import org.example.btvnkotlin.Week6_03_17.ui.OrderViewModel
import org.example.btvnkotlin.Week6_03_17.ui.SelectOptionScreen
import org.example.btvnkotlin.Week6_03_17.ui.StartOrderScreen
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


// 1. ĐỊNH NGHĨA CÁC ROUTE (ĐIỂM ĐẾN) BẰNG ENUM CLASS
// Mỗi màn hình sẽ có một cái tên (name) và một tiêu đề (title) tương ứng để hiện lên TopAppBar
enum class CupcakeScreen(val title: StringResource) {
    Start(title = Res.string.app_name),
    Flavor(title = Res.string.choose_flavor),
    Pickup(title = Res.string.choose_pickup_date),
    Summary(title = Res.string.order_summary)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CupcakeAppBar(
    currentScreen: CupcakeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun CupcakeApp(
    viewModel: OrderViewModel = viewModel { OrderViewModel() },
    navController: NavHostController = rememberNavController()
) {
    // Lấy thông tin màn hình hiện tại từ BackStack để đổi tên trên thanh tiêu đề
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CupcakeScreen.valueOf(
        backStackEntry?.destination?.route ?: CupcakeScreen.Start.name
    )

    Scaffold(
        topBar = {
            CupcakeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() } // Lệnh quay lại màn hình trước
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        // 2. NAVHOST: "SÂN KHẤU" CHÍNH
        NavHost(
            navController = navController,
            startDestination = CupcakeScreen.Start.name, // Mở app lên là vào màn hình Start
            modifier = Modifier.padding(innerPadding)
        ) {
            // Màn hình 1: Chọn số lượng bánh
            composable(route = CupcakeScreen.Start.name) {
                StartOrderScreen(
                    quantityOptions = DataSource.quantityOptions,
                    // Khi bấm nút, cập nhật ViewModel và lái xe sang màn hình Flavor
                    onNextButtonClicked = { quantity ->
                        viewModel.setQuantity(quantity)
                        navController.navigate(CupcakeScreen.Flavor.name)
                    },
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                )
            }

            // Màn hình 2: Chọn hương vị
            composable(route = CupcakeScreen.Flavor.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = DataSource.flavors.map { stringResource(it) }, // Lấy danh sách tên vị
                    onSelectionChanged = { viewModel.setFlavor(it) },
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Pickup.name) },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            // Màn hình 3: Chọn ngày lấy bánh
            composable(route = CupcakeScreen.Pickup.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = uiState.pickupOptions,
                    onSelectionChanged = { viewModel.setDate(it) },
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) },
                    modifier = Modifier.fillMaxHeight()
                )
            }

            // Màn hình 4: Tóm tắt đơn hàng
            composable(route = CupcakeScreen.Summary.name) {
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) },
                    onSendButtonClicked = { subject: String, summary: String ->
                        // LƯU Ý KMP: Trong Android bài Lab, Google dùng Intent để mở app Email.
                        // Nhưng KMP không có Intent. Ở đây chúng ta tạm in ra Console để check logic.
                        println("ĐÃ GỬI ĐƠN HÀNG: \nTiêu đề: $subject \nNội dung: $summary")
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

// Hàm hỗ trợ: Hủy đơn hàng và quay về màn hình đầu tiên (Xóa sạch Stack)
private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(CupcakeScreen.Start.name, inclusive = false)
}