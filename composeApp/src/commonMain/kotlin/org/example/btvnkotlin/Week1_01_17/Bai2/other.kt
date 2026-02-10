package Bai2

// Nhập từ thư viện toán học Kotlin
import kotlin.math.PI

// Lớp đối tượng để dùng cho ví dụ 'with'
class DonHangPizza(val tenBanh: String, var kichThuocCm: Double, var voBanh: String) {
    fun nuongBanh() {
        println("...Đang nướng bánh $tenBanh...")
    }
}

fun main() {
    // Các thao tác chỉ định tăng cường
    println("--- TÍNH HÓA ĐƠN ---")
    var tongTien = 100.0
    println("Giá gốc: $tongTien")

    tongTien += 20.0
    println("Sau khi cộng ship: $tongTien")

    tongTien -= 10.0
    println("Sau khi giảm giá: $tongTien")

    tongTien *= 1.1
    println("Tổng thanh toán (VAT 10%): $tongTien")
    println("\n----------------\n")

    // Sử dụng with để đơn giản hóa việc truy cập vào một đối tượng
    println("--- THÔNG TIN ĐƠN HÀNG ---")
    val donHangCuaToi = DonHangPizza("Hải Sản", 30.0, "Viền phô mai")

    // Thay vì viết: donHangCuaToi.tenBanh, donHangCuaToi.kichThuocCm...
    with(donHangCuaToi) {
        println("Tên bánh: $tenBanh")       // Truy cập trực tiếp thuộc tính
        println("Kích thước: ${kichThuocCm}cm")
        println("Loại vỏ: $voBanh")
        nuongBanh()                         // Gọi phương thức trực tiếp
    }
    println("\n----------------\n")

    println("--- TÍNH DIỆN TÍCH BÁNH ---")
    val banKinh = donHangCuaToi.kichThuocCm / 2

    // Cách 1: Dùng PI đã import ở trên
    val dienTich = PI * banKinh * banKinh
    println("Diện tích mặt bánh (Dùng Import): $dienTich cm2")

    // Cách 2: Dùng tên đủ điều kiện (Không cần import)
    val chuVi = 2 * kotlin.math.PI * banKinh
    println("Chu vi viền bánh (Dùng Full Name): $chuVi cm")
    println("\n----------------\n")

    // Xâu chuỗi các lệnh gọi với nhau
    println("--- XỬ LÝ GHI CHÚ KHÁCH HÀNG ---")
    val ghiChuTho = "   giao hàng  lúc   12h trưa   "

    // Chuỗi lệnh: Cắt khoảng trắng thừa -> Viết hoa -> Thay thế từ
    val ghiChuSach = ghiChuTho.trim().uppercase().replace("LÚC", "VÀO")

    println("Ghi chú gốc: '$ghiChuTho'")
    println("Sau khi xử lý: '$ghiChuSach'")
    println("\n----------------\n")

    // Số lượng đối số hàm có thể thay đổi
    println("--- THÊM TOPPING TÙY Ý ---")

    // Gọi hàm với 1 tham số
    themTopping("Xúc xích")

    // Gọi hàm với 3 tham số (nhờ vararg)
    themTopping("Nấm", "Ớt chuông", "Thịt bò")
}

// Hàm hỗ trợ vararg
fun themTopping(vararg danhSachTopping: String) {
    print("Đang thêm các loại: ")
    for (mon in danhSachTopping) {
        print("$mon, ")
    }
    println("-> Hoàn tất!")
}