package Bai2

fun main() {
    // Tạo thực thể từ lớp con (Thực tập sinh)
    val thucTap = ThucTapSinh()
    println("--- ${thucTap.chucVu} ---")
    println("Lương thực nhận: ${thucTap.tinhLuong()}đ")

    println()

    // Tạo thực thể từ lớp con kế thừa lớp mở (Trưởng phòng)
    val sep = TruongPhong(luongCung = 10_000_000.0)
    println("--- ${sep.chucVu} ---")
    println("Lương thực nhận: ${sep.tinhLuong()}đ")
}

// Lớp trừu tượng (Không thể tạo đối tượng trực tiếp từ lớp này)
abstract class NhanVien {

    // Lớp trừu tượng có một thuộc tính trừu tượng
    // (Các lớp con BẮT BUỘC phải khai báo giá trị cho biến này)
    abstract val chucVu: String

    // Lớp trừu tượng có một phương thức trừu tượng
    // (Các lớp con BẮT BUỘC phải viết code xử lý cho hàm này)
    abstract fun tinhLuong(): Double
}

// Đánh dấu một lớp là 'open' để lớp đó có thể phân lớp con
open class NhanVienChinhThuc(val luongCung: Double) : NhanVien() {
    override val chucVu = "Nhân Viên Chính Thức"

    override fun tinhLuong(): Double {
        return luongCung
    }
}

// Tạo lớp con bằng cách mở rộng lớp mẹ
class ThucTapSinh : NhanVien() {
    override val chucVu = "Thực Tập Sinh"

    override fun tinhLuong(): Double {
        return 0.0
    }
}

// Tạo lớp con từ một lớp 'open'
class TruongPhong(luongCung: Double) : NhanVienChinhThuc(luongCung) {

    override val chucVu = "Trưởng Phòng"

    // Gọi quy trình triển khai lớp cao cấp (super)
    override fun tinhLuong(): Double {
        // Gọi hàm tinhLuong() của lớp cha (NhanVienChinhThuc) để lấy lương cứng
        return super.tinhLuong() + 5_000_000.0
    }
}