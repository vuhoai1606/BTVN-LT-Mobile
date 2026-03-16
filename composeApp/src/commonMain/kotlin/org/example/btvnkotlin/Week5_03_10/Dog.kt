package org.example.btvnkotlin.Week5_03_10

import btvnkotlin.composeapp.generated.resources.*
import btvnkotlin.composeapp.generated.resources.bella
import btvnkotlin.composeapp.generated.resources.faye
import btvnkotlin.composeapp.generated.resources.frankie
import btvnkotlin.composeapp.generated.resources.koda
import btvnkotlin.composeapp.generated.resources.lola
import btvnkotlin.composeapp.generated.resources.nox
import org.jetbrains.compose.resources.DrawableResource

// data class: Compiler tự động tạo các hàm getter/setter, equals(), hashcode() ở tầng Bytecode.
data class Dog(
    val imageResourceId: DrawableResource,
    val name: String,
    val age: Int,
    val hobbies: String
)

val dogs = listOf(
    Dog(Res.drawable.koda, "Koda", 2, "Thích ăn xúc xích và chạy đuổi theo xe máy"),
    Dog(Res.drawable.lola, "Lola", 16, "Ngủ cả ngày, đôi khi sủa người lạ"),
    Dog(Res.drawable.frankie, "Frankie", 2, "Gặm giày của chủ"),
    Dog(Res.drawable.nox, "Nox", 8, "Biết bắt tay và giả chết"),
    Dog(Res.drawable.faye, "Faye", 8, "Đào hố ngoài sân vườn"),
    Dog(Res.drawable.bella, "Bella", 14, "Thích được gãi bụng")
)