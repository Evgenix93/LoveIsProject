package com.project.loveis.models

import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class ChatMessage(
    val id: Long,
    val user: Sender,
    val attachments: List<Map<String, String>>,
    val content: String?,
    val timestamp: String
)
