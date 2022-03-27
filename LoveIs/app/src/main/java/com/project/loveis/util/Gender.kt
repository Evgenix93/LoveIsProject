package com.project.loveis.util

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

enum class Gender {
    @Json(name = "male")
    Male,
    @Json(name = "female")
    Female
}