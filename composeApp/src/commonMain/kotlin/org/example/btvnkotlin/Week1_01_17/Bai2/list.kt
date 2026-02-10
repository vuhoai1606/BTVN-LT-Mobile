package Bai2

fun main() {
    println("--- Tổng kết điểm thi ---")

    // Xác định một danh sách chỉ có thể đọc
    val diemThi = listOf(8.5, 9.0, 7.5, 10.0, 6.5)

    // Lấy kích thước của danh sách
    println("Tổng số bài thi: ${diemThi.size}")

    // Lấy mục đầu tiên trong danh sách
    println("Điểm bài đầu tiên: ${diemThi[0]}")

    // Lấy bản sao danh sách theo thứ tự đảo ngược
    val diemNguoc = diemThi.reversed()
    println("Danh sách đảo ngược: $diemNguoc")

    println("\n----------------\n")

    println("--- Danh sách việc cần làm (To-Do List) ---")

    // Xác định một danh sách các chuỗi có thể thay đổi
    val viecCanLam = mutableListOf<String>()
    println("Ban đầu: $viecCanLam")

    // Thêm mục vào danh sách
    viecCanLam.add("Đi chợ")
    viecCanLam.add("Nấu cơm")
    viecCanLam.add("Ngủ trưa")
    println("Sau khi thêm việc: $viecCanLam")

    // Sửa đổi một mục trong danh sách
    viecCanLam[0] = "Đặt đồ ăn ShopeeFood"
    println("Sau khi sửa việc đầu tiên: $viecCanLam")

    // Xóa một mục khỏi danh sách (.remove)
    viecCanLam.remove("Ngủ trưa")
    println("Sau khi xóa việc đã làm: $viecCanLam")
}