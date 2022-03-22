package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Type(
    val id: Int,
    val icon: String,
    val name: String,
    val locked: Boolean
)
