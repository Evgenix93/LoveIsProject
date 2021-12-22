package com.project.loveis.models

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass (generateAdapter = true)
data class Place(
    val id: Int,
    val address: String,
    val name: String,
    val photo: String
)
