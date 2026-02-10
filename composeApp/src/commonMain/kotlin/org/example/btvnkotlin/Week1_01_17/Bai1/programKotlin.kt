package Bai1

fun main() {
    val ten = "Vũ"
    val tuoi = 19
    println("Người chơi: $ten ($tuoi tuổi)")

    batDauGame()

    val diemCuaBan = layDiemXucXac()
    val diemCuaMay = layDiemXucXac()

    println("Điểm của bạn: $diemCuaBan")
    println("Điểm của máy: $diemCuaMay")

    soSanhDiem(diemCuaBan, diemCuaMay)
}

// Hàm không có đối số
fun batDauGame() {
    println("--- Bắt đầu gieo xúc xắc ---")
}

// Hàm trả về giá trị Int (Tạo ngẫu nhiên 1-6)
fun layDiemXucXac(): Int {
    return (1..6).random()
}

// Hàm có đối số (dùng để so sánh)
fun soSanhDiem(diem1: Int, diem2: Int) {
    if (diem1 > diem2) {
        println("=> Kết quả: YOU WIN!")
    } else if (diem1 < diem2) {
        println("=> Kết quả: YOU LOSE!")
    } else {
        println("=> Kết quả: DRAW!")
    }
}