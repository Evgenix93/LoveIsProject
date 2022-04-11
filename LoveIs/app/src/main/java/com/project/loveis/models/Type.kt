package com.project.loveis.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Type(
    val id: Long,
    val icon: String,
    @Json(name = "icon_active")
    val iconActive: String,
    val name: String,
    val locked: Boolean,
    val isChecked: Boolean?
)
