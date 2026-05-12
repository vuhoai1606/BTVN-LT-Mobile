package org.example.btvnkotlin.Week7_03_24.model

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    var courseID: String = "",
    var courseName: String = "",
    var courseDuration: String = "",
    var courseDescription: String = ""
)