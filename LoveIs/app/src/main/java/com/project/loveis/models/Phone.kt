package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Phone(
    val phone: String
)
