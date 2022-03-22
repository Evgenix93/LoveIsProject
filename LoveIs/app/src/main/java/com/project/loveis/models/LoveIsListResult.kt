package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoveIsListResult(
    val page: Int,
    val size: Int,
    val total: Int,
    val list: List<LoveIs>

)
