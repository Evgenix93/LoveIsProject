package com.project.loveis.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class Dialog(
    @Json(name = "chat_with")
    val chatWith: User,
    val list: List<ChatMessage>?,
    @Json(name = "last_message")
    val lastMessage: ChatMessage?,
    val total: Int?,
    val size: Int?,
    val page: Int?
)
