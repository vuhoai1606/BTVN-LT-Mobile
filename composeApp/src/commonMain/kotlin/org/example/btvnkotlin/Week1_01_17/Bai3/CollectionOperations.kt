package Bai3

fun main() {
    // THAO TÁC TRÊN MAP
    val bangDiem = mapOf(
        "Minh" to 8.5,
        "Lan" to 9.0,
        "Hùng" to 4.5,
        "Tuấn" to 6.0
    )

    println("--- Lặp lại tập hợp (forEach) ---")
    bangDiem.forEach {
        print("Học sinh ${it.key} đạt ${it.value} điểm. ")
    }
    println("\n")

    println("--- Chuyển đổi từng mục trong một tập hợp ---")
    // .map: Biến đổi từng mục từ "Minh=8.5" thành chuỗi "Minh: 8.5"
    // .joinToString: Nối tất cả lại bằng dấu gạch đứng " | "
    val baoCao = bangDiem.map { "${it.key}: ${it.value}" }.joinToString(" | ")
    println(baoCao)

    println("\n--- Lọc các mục trong một tập hợp ---")
    // Lọc ra các học sinh Giỏi (Điểm >= 8.0)
    val hocSinhGioi = bangDiem.filter { it.value >= 8.0 }
    println("Danh sách học sinh giỏi: $hocSinhGioi")
    println("\n----------------\n")


    // CHUỖI CÁC PHÉP TOÁN TRÊN LIST
    println("--- Chain Operations (Chuỗi lệnh liên tiếp) ---")

    val khoQuaTang = listOf("Bánh quy", "Áo mưa", "Bút bi", "Balo", "Bóng bay", "Kẹo ngọt", "Bình nước")

    // Yêu cầu: Chọn quà bắt đầu bằng chữ "B", lấy ngẫu nhiên 2 món, rồi xếp theo ABC
    val quaDuocChon = khoQuaTang
        .filter { it.startsWith("B", ignoreCase = true) } // 1. Lọc lấy: Bánh, Bút, Balo, Bóng, Bình
        .shuffled() // 2. Xáo trộn thứ tự ngẫu nhiên (Mỗi lần chạy sẽ khác nhau)
        .take(2)    // 3. Chỉ lấy ra 2 món đầu tiên sau khi xáo
        .sorted()   // 4. Sắp xếp 2 món đó theo thứ tự Alpha B

    println("Quà tặng ngẫu nhiên hôm nay: $quaDuocChon")
}