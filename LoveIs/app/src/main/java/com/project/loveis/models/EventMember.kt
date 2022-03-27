package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventMember(
    val id: Long,
    val users: String,
    val user: Long,
    val event: Long,
    )
