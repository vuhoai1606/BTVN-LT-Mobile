package Bai1

fun main() {
    // IF / ELSE
    val diem = 9
    if (diem >= 8.5) {
        println("Điểm A.")
    } else if (diem < 8.5 && diem >= 7) {
        println("Điểm B.")
    } else if (diem < 7 && diem >= 5.5) {
        println("Điểm C.")
    } else if (diem < 5.5 && diem >= 4) {
        println("Điểm D.")
    } else {
        println("Điểm F (không đạt!)")
    }

    println("\n----------------\n")

    // WHEN
    val phimBam = "A"

    print("Hành động: ")
    when (phimBam) {
        "W" -> println("Đi lên")
        "S" -> println("Đi xuống")
        "A" -> println("Sang trái")
        "D" -> println("Sang phải")
        else -> println("Đứng yên")
    }

    println("\n----------------\n")

    // REPEAT

    goiTen("Vũ")

    println()

    veHinhChuNhat(3, 5) // repeat lồng nhau
}

// REPEAT đơn
fun goiTen(ten: String) {
    print("Đang gọi $ten: ")
    repeat(3) {
        print("$ten! ")
    }
    println()
}

// REPEAT LỒNG NHAU
fun veHinhChuNhat(hang: Int, cot: Int) {
    println("Vẽ hình chữ nhật $hang x $cot:")

    repeat(hang) {
        repeat(cot) {
            print("* ")
        }
        println()
    }
}