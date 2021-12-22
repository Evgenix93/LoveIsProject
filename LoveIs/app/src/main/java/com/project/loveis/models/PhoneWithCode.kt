package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhoneWithCode(
    val phone: String,
    val code: String
)
