package Bai3

fun main() {
    // Tạo một nhóm từ danh sách
    println("--- Lọc hồ sơ ứng viên ---")

    val danhSachTho = listOf("Tiếng Anh", "Word", "Excel", "Tiếng Anh", "PowerPoint", "Word")
    val kyNangChinhThuc = danhSachTho.toSet()

    println("Danh sách gốc: $danhSachTho")
    println("Danh sách chuẩn: $kyNangChinhThuc")
    println("\n----------------\n")


    // Xác định một nhóm
    // Yêu cầu của công ty (Cố định - setOf)
    val yeuCauCongTy = setOf("Kotlin", "Java", "SQL")
    println("Yêu cầu của công ty: $yeuCauCongTy")

    // Kỹ năng của Ứng viên (Có thể thay đổi/học thêm - mutableSetOf)
    val kyNangUngVien = mutableSetOf("Java", "Python", "HTML")
    println("Kỹ năng ứng viên ban đầu: $kyNangUngVien")
    kyNangUngVien.add("SQL")
    println("Kỹ năng ứng viên sau đó: $kyNangUngVien")
    println("\n----------------\n")


    // Phép toán trên nhóm
    println("--- Kết quả phỏng vấn ---")

    // a. Intersect (Giao nhau): Tìm những gì cả 2 bên ĐỀU CÓ
    val kyNangDatYeuCau = yeuCauCongTy.intersect(kyNangUngVien)
    println("Kỹ năng phù hợp (Intersect): $kyNangDatYeuCau")

    // b. Union (Hợp lại): Gộp tất cả lại thành một danh sách duy nhất
    val tongHopCongNghe = yeuCauCongTy.union(kyNangUngVien)
    println("Tổng hợp Tech Stack (Union): $tongHopCongNghe")
}