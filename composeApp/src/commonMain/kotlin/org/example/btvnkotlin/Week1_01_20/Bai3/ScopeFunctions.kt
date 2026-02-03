package Bai3

fun main() {
    // HÀM PHẠM VI: LET
    // (Thường dùng để kiểm tra null an toàn và xử lý biến đó bằng từ khóa 'it')
    val goiDuLieu: Map<String, String>? = mapOf("MA_VE" to "V12345", "PHIM" to "Mưa đỏ")
    var maVeHienTai = ""

    // Cú pháp: object?.let { ... }
    goiDuLieu?.let {
        // Trong này, 'it' chính là 'goiDuLieu'
        println("Đã nhận được dữ liệu!")

        // Lấy dữ liệu từ 'it' gán vào biến bên ngoài
        maVeHienTai = it["MA_VE"].toString()

        println("Đang xử lý vé số: $maVeHienTai")
    }
    println("\n----------------\n")


    // HÀM PHẠM VI: APPLY
    // (Thường dùng để khởi tạo/cấu hình đối tượng. Dùng 'this' hoặc gọi thẳng thuộc tính)
    var gheNgoi: GheRapPhim? = GheRapPhim()

    // Cú pháp: object?.apply { ... }
    gheNgoi?.apply {
        // Trong này, 'this' chính là đối tượng 'gheNgoi'
        // Ta có thể gọi trực tiếp các thuộc tính và phương thức của nó mà không cần gõ 'gheNgoi.' lặp lại
        soGhe = "A15"
        loaiGhe = "VIP"
        daDuocDat = true

        inThongTin()
    }
}

class GheRapPhim {
    var soGhe: String = ""
    var loaiGhe: String = "Thường"
    var daDuocDat: Boolean = false

    fun inThongTin() {
        println("Cấu hình ghế hoàn tất: Ghế $soGhe ($loaiGhe) - Đã đặt: $daDuocDat")
    }
}