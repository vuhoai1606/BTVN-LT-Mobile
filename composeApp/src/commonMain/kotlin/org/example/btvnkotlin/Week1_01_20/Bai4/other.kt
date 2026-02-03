package Bai4

fun main() {
    // --- TRUY CẬP GIÁ TRỊ ENUM ---
    // Giả sử đơn hàng vừa được tạo
    val trangThaiHienTai = TrangThaiDonHang.DANG_GIAO
    println("Trạng thái khởi tạo: $trangThaiHienTai")

    // Gọi hàm xử lý trong Object Singleton
    QuanLyDonHang.capNhatTrangThai(trangThaiHienTai)
    println("\n----------------\n")

    // --- XỬ LÝ NGOẠI LỆ (TRY / CATCH) ---
    // Gọi hàm giả lập lỗi kết nối database
    QuanLyDonHang.luuVaoCoSoDuLieu()
}

// --- TẠO LỚP ENUM (Liệt kê các trạng thái cố định) ---
enum class TrangThaiDonHang {
    CHO_XAC_NHAN,
    DANG_GIAO,
    DA_GIAO_HANG,
    DA_HUY
}

// --- KHAI BÁO MỘT ĐỐI TƯỢNG (OBJECT - SINGLETON) ---
// 'object' trong Kotlin tạo ra một Singleton (chỉ có duy nhất 1 bản thể trong toàn bộ ứng dụng)
object QuanLyDonHang {

    fun capNhatTrangThai(trangThai: TrangThaiDonHang) {
        print("Hệ thống xử lý: ")

        when (trangThai) {
            TrangThaiDonHang.CHO_XAC_NHAN -> {
                println("Gửi thông báo cho chủ shop đóng gói.")
            }
            TrangThaiDonHang.DANG_GIAO -> {
                println("Gửi vị trí shipper cho khách hàng.")
            }
            TrangThaiDonHang.DA_GIAO_HANG -> {
                println("Cộng điểm tích lũy cho khách.")
            }
            TrangThaiDonHang.DA_HUY -> {
                println("Hoàn tiền lại vào ví.")
            }
        }
    }

    // Hàm minh họa phát hiện ngoại lệ
    fun luuVaoCoSoDuLieu() {
        println("Đang kết nối Database...")

        try {
            // Giả lập một lỗi xảy ra (ví dụ: mất mạng)
            throw Exception("Mất kết nối máy chủ!")
        } catch (loi: Exception) {
            // Bắt lấy lỗi và xử lý nhẹ nhàng thay vì để app bị crash (văng)
            println("CẢNH BÁO: Đã xảy ra lỗi - ${loi.message}")
            println("-> Đang chuyển sang chế độ lưu Offline.")
        }
    }
}