package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenData(
    val phone: String,
    val password: String
)
