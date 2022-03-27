package com.project.loveis.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Sender(
    val id: Long,
    @Json(name = "username")
    val userName: String,
    val name: String,
    val photo: String
)
