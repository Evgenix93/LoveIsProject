package com.project.loveis.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateEventIsRequest(
    val date: String,
    @Json(name = "telegram_url")
    val telegramUrl: String?,
    @Json(name = "whatsapp_url")
    val whatsAppUrl: String?,
    val type: Int,
    val place: Int,
    val price: Int,
    @Json(name = "persons_number")
    val personsNumber: Int,
    val name: String
)
