package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TypesWrapper(
    val list: List<Type>
)
