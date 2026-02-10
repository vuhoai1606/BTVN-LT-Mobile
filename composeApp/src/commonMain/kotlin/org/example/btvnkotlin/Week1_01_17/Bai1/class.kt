package Bai1

fun main() {
    // TẠO THỰC THỂ
    val dongXuCuaToi = DongXu() // Khởi tạo

    dongXuCuaToi.tungDongXu()

    println("\n----------------\n")

    // Tạo một vòng quay có 100 ô số
    val vongQuayLon = VongQuay(100)
    val ketQua1 = vongQuayLon.quay()
    println("Kết quả quay vòng lớn (1-100): Trúng số $ketQua1")

    // Tạo một vòng quay nhỏ chỉ có 10 ô số
    val vongQuayNho = VongQuay(10)
    val ketQua2 = vongQuayNho.quay()
    println("Kết quả quay vòng nhỏ (1-10): Trúng số $ketQua2")
}

// --- ĐỊNH NGHĨA CLASS ---
//lớp không có tham số
class DongXu {
    fun tungDongXu() {
        val ketQua = (1..2).random()

        if (ketQua == 1) {
            println("Kết quả: Mặt Ngửa")
        } else {
            println("Kết quả: Mặt Sấp")
        }
    }
}

// lơp có tham số
class VongQuay(val soO: Int) {
    fun quay(): Int {
        // Random từ 1 đến số ô được cài đặt lúc tạo
        val soTrungThuong = (1..soO).random()
        return soTrungThuong
    }
}