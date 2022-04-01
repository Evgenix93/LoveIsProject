package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseErrorsMap(
    val errors: Map<String, Array<String>>
)
