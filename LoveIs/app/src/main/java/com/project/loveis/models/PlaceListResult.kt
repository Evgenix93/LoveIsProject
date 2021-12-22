package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaceListResult(
    val page: Int,
    val size: Int,
    val total: Int,
    val list: List<Place>
)
