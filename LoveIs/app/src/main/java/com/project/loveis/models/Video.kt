package com.project.loveis.models

import com.project.loveis.util.Gender
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Video(
    val id: Long,
    val key: String,
    val url: String,
    val gender: Gender,
    val name: String,
    val description: String
)
