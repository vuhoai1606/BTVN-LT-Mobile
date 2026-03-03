package org.example.btvnkotlin.Week4_03_03

fun calculateTip(amount: Double, tipPercent: Double): String {
    var tip = tipPercent / 100 * amount

    // Trả về chuỗi định dạng 2 chữ số thập phân
    return tip.toString()
}