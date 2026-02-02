package org.example.btvnkotlin

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform