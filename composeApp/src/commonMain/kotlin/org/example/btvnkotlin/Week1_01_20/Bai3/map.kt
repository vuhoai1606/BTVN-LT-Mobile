package Bai3

fun main() {
    // Xác định một sơ đồ có thể thay đổi
    // Cấu trúc: <Tên sản phẩm, Số lượng tồn kho>
    val khoHang = mutableMapOf<String, Int>(
        "Laptop Gaming" to 10,
        "Chuột không dây" to 50
    )
    println("Kho hàng ban đầu: $khoHang")
    println("\n----------------\n")


    // Đặt một giá trị trong sơ đồ có thể thay đổi
    // Cách 1: Sử dụng hàm .put()
    khoHang.put("Bàn phím cơ", 30)
    println("Sau khi dùng .put(): $khoHang")

    // Cách 2: Sử dụng cú pháp ngoặc vuông []
    khoHang["Tai nghe Bluetooth"] = 15

    // Cập nhật lại giá trị của khóa đã tồn tại
    khoHang["Laptop Gaming"] = 8

    println("Sau khi dùng []: $khoHang")
}