package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventMembersListResult(
    val total: Int,
    val events: List<User>
)
