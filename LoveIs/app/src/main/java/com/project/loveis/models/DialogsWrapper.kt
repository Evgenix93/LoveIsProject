package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DialogsWrapper(
     val list: List<Dialog>,
     val total: Int,
     val size: Int,
     val page: Int
)
