package Bai2

fun main() {
    val gioHang = listOf("Thịt bò", "Rau cải", "Trứng gà", "Sữa tươi")

    println("--- DÙNG FOR (Duyệt từng món) ---")
    for (monDo in gioHang) {
        println("Đã cho vào giỏ hành: $monDo")
    }

    println("\n----------------\n")

    println("--- DÙNG WHILE (Duyệt theo vị trí) ---")
    var viTri = 1

    while (viTri < gioHang.size) {
        val tenMon = gioHang[viTri]
        println("Món thứ ${viTri + 1} là: $tenMon")

        viTri++
    }
}