package Bai4

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

// 1. CÁC HÀM PHA CHẾ (SUSPEND) - Các công việc tốn thời gian
suspend fun phaCaPhe(): String {
    println("   [Bar] Bắt đầu xay hạt và pha cà phê...")
    delay(2000L)
    println("   [Bar] -> Cà phê đã xong!")
    return "Cà phê sữ đá"
}

suspend fun nuongBanhMi(): String {
    println("   [Bếp] Bắt đầu nướng bánh...")
    delay(1500L)
    println("   [Bếp] -> Bánh mì đã giòn!")
    return "Bánh mì ốp la"
}

fun main() = runBlocking {
    println("--- QUÁN CAFE MỞ CỬA ---")
    println("Khách hàng: 'Cho tôi 1 Combo sáng (Bánh + Nước)'")

    // --- LIÊN KẾT 1: CHẠY SONG SONG ĐỂ HOÀN THÀNH 1 MỤC TIÊU ---
    // Để trả đơn hàng nhanh nhất, Bếp và Bar phải làm cùng lúc (Async)

    val thoiGianCheBien = measureTimeMillis {
        println("\n--- Bắt đầu chuẩn bị đơn hàng ---")

        // Bar làm việc của Bar
        val taskNuoc = async {
            phaCaPhe()
        }

        // Bếp làm việc của Bếp
        val taskBanh = async {
            nuongBanhMi()
        }

        // --- LIÊN KẾT 2: TÁC VỤ PHỤ TRỢ (JOB) ---
        // Trong lúc 2 ông kia đang làm, nhân viên phục vụ tranh thủ lau bàn
        // Việc lau bàn (launch) chạy độc lập nhưng nằm trong quy trình phục vụ khách này
        val viecLauBan = launch {
            println("   [Phục vụ] Tranh thủ ra lau bàn cho khách...")
            delay(1000L)
            println("   [Phục vụ] -> Bàn đã sạch sẽ.")
        }

        // --- LIÊN KẾT 3: GOM KẾT QUẢ (AWAIT) ---
        // Phục vụ phải đợi CẢ HAI món xong mới bưng ra được (Đồng bộ hóa kết quả)
        val monNuoc = taskNuoc.await()
        val monBanh = taskBanh.await()

        println("\n>>> TING TING! Đơn hàng hoàn tất: $monBanh và $monNuoc")
    }

    println("(Tổng thời gian chờ của khách: $thoiGianCheBien ms)\n")


    // --- LIÊN KẾT 4: XỬ LÝ SỰ CỐ / HỦY ĐƠN (CANCEL) ---
    println("--- Tình huống: Khách đổi ý ---")
    println("Khách: 'Thôi cho tôi đổi món, đừng làm Sinh tố nữa!'")

    // Bắt đầu làm sinh tố
    val jobSinhTo = launch {
        println("   [Bar] Đang xay sinh tố bơ...")
        delay(3000L)
        println("   [Bar] Xong sinh tố!") // Dòng này sẽ không kịp in ra
    }

    delay(1000L) // Làm được 1 giây thì khách kêu hủy
    println("Quản lý: 'Dừng lại! Khách hủy đơn!'")

    jobSinhTo.cancel()
    jobSinhTo.join()

    println("-> Đã hủy món sinh tố, không tính tiền.")
}