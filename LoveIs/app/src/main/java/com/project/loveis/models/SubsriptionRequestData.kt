package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubsriptionRequestData(
    val value: Int,
    val until: String

)
