package com.project.loveis.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventIs(
    val id: Int,
    val date: String,
    val owner: User,
    @Json(name = "participants_count")
    val participantsCount: String,
    @Json(name = "telegram_url")
    val telegramUrl: String?,
    @Json(name = "whatsapp_url")
    val whatsAppUrl: String?,
    val status: String,
    val place: Place,
    val type: Type,
    @Json(name = "persons_number")
    val personsNumber: Int,
    val price: Int
)
